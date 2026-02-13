package com.anshu.imageservice.service;

import com.anshu.imageservice.dto.ImageUploadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface ImageService {
    ImageUploadResponse uploadImage(UUID deviceUuid, String deviceSecret, /*MultipartFile file*/ byte[] imageBytes);
}
