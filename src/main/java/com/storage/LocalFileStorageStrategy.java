package com.storage;

import com.storage.interfaces.FileStorageStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;


@Component
public class LocalFileStorageStrategy implements FileStorageStrategy {
    @Override
    public String save(MultipartFile file, String folder, String prefix) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }
        Path uploadPath = Paths.get(folder).toAbsolutePath().normalize();
        Files.createDirectories(uploadPath);
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        }
        String filename = prefix + "_" + UUID.randomUUID() + extension;
        Path targetLocation = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        return targetLocation.toString();
    }

    @Override
    public boolean delete(String filePath) {
        try {
            Path path = Paths.get(filePath);
            return Files.deleteIfExists(path);
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public String getStorageType() {
        return "LOCAL";
    }
}

