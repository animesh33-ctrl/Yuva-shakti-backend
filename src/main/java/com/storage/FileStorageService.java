package com.storage;

import com.storage.interfaces.FileStorageStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    private final FileStorageStrategy fileStorageStrategy;

    public String save(MultipartFile file, String folder, String prefix) throws IOException {
        return fileStorageStrategy.save(file, folder, prefix);
    }

    public boolean delete(String filePath) {
        return fileStorageStrategy.delete(filePath);
    }

    public String getStorageType() {
        return fileStorageStrategy.getStorageType();
    }
}