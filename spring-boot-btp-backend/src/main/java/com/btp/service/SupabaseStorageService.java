package com.btp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@ConditionalOnProperty(name = "app.storage.type", havingValue = "supabase")
public class SupabaseStorageService implements FileStorageService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.service-key}")
    private String serviceKey;

    @Value("${supabase.bucket:documents}")
    private String bucket;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String save(MultipartFile file) throws IOException {
        String ext = getExtension(file.getOriginalFilename());
        String filename = UUID.randomUUID() + ext;

        String uploadUrl = supabaseUrl + "/storage/v1/object/" + bucket + "/" + filename;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + serviceKey);
        headers.setContentType(MediaType.parseMediaType(
            file.getContentType() != null ? file.getContentType() : "application/octet-stream"
        ));

        restTemplate.exchange(uploadUrl, HttpMethod.POST,
            new HttpEntity<>(file.getBytes(), headers), String.class);

        return getPublicUrl(filename);
    }

    @Override
    public Optional<Resource> loadAsResource(String fileRef) {
        // Files live on Supabase — caller should redirect to getPublicUrl() instead.
        return Optional.empty();
    }

    @Override
    public String getPublicUrl(String fileRef) {
        // fileRef may already be a full URL (stored that way in DB).
        if (fileRef.startsWith("http")) return fileRef;
        return supabaseUrl + "/storage/v1/object/public/" + bucket + "/" + fileRef;
    }

    @Override
    public void delete(String fileRef) {
        try {
            String filename = fileRef.startsWith("http")
                ? fileRef.substring(fileRef.lastIndexOf('/') + 1)
                : fileRef;

            String deleteUrl = supabaseUrl + "/storage/v1/object/" + bucket;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + serviceKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> body = Map.of("prefixes", List.of(filename));
            restTemplate.exchange(deleteUrl, HttpMethod.DELETE,
                new HttpEntity<>(body, headers), String.class);
        } catch (Exception e) {
            log.warn("Could not delete Supabase file {}: {}", fileRef, e.getMessage());
        }
    }

    private String getExtension(String filename) {
        if (filename == null) return "";
        int dot = filename.lastIndexOf('.');
        return dot > 0 ? filename.substring(dot) : "";
    }
}
