package com.btp.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@ConditionalOnProperty(name = "app.storage.type", havingValue = "local", matchIfMissing = true)
public class LocalFileStorageService implements FileStorageService {

    @Value("${app.storage.local.path:uploads}")
    private String uploadPath;

    @Value("${app.backend.url:http://localhost:8080/api}")
    private String backendUrl;

    private Path root;

    @PostConstruct
    public void init() throws IOException {
        root = Paths.get(uploadPath);
        Files.createDirectories(root);
        log.info("Local file storage ready at: {}", root.toAbsolutePath());
    }

    @Override
    public String save(MultipartFile file) throws IOException {
        String ext = getExtension(file.getOriginalFilename());
        String filename = UUID.randomUUID() + ext;
        Files.copy(file.getInputStream(), root.resolve(filename));
        return filename;
    }

    @Override
    public Optional<Resource> loadAsResource(String fileRef) {
        try {
            Path filePath = root.resolve(fileRef);
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return Optional.of(resource);
            }
        } catch (MalformedURLException ignored) {}
        return Optional.empty();
    }

    @Override
    public String getPublicUrl(String fileRef) {
        return backendUrl + "/files/" + fileRef;
    }

    @Override
    public void delete(String fileRef) {
        try {
            Files.deleteIfExists(root.resolve(fileRef));
        } catch (IOException e) {
            log.warn("Could not delete local file {}: {}", fileRef, e.getMessage());
        }
    }

    private String getExtension(String filename) {
        if (filename == null) return "";
        int dot = filename.lastIndexOf('.');
        return dot > 0 ? filename.substring(dot) : "";
    }
}
