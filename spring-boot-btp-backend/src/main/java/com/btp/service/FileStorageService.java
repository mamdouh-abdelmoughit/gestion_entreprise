package com.btp.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface FileStorageService {

    /**
     * Saves the file and returns a reference stored in the DB.
     * Local impl returns a UUID filename; Supabase impl returns the full public URL.
     */
    String save(MultipartFile file) throws IOException;

    /**
     * Returns the file as a streamable Resource.
     * Returns empty when the file lives externally (redirect via getPublicUrl instead).
     */
    Optional<Resource> loadAsResource(String fileRef);

    /**
     * Returns a direct URL for the file reference.
     * Used for download/preview redirects when loadAsResource() is empty.
     */
    String getPublicUrl(String fileRef);

    /**
     * Deletes the file from storage. Silent on missing files.
     */
    void delete(String fileRef);
}
