package com.storage.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageStrategy {

    String save(
            MultipartFile file,
            String folder,
            String prefix)
            throws IOException;

    boolean delete(String filePath);

    String getStorageType();
}