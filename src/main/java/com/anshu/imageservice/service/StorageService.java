package com.anshu.imageservice.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    String store(/*MultipartFile file*/  byte[] imageBytes, String imageUuid);
}
