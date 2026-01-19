package com.anshu.imageservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Profile("local")
@Slf4j
public class LocalStorageServiceImpl implements StorageService {

    @Value("${image.storage.local.path}")
    private String basePath;

    @Override
    public String store(byte[] bytes, String uuid) {

        try {
            Path dir = Paths.get(basePath);
            Files.createDirectories(dir);

            Path path = dir.resolve(uuid + ".jpg");
            Files.write(path, bytes);

            return path.toString();

        } catch (IOException e) {
            throw new RuntimeException("Storage failed", e);
        }
    }
}
