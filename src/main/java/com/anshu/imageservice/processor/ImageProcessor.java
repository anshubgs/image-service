package com.anshu.imageservice.processor;


import com.anshu.imageservice.event.ImageUploadedEvent;
import com.anshu.imageservice.model.Image;
import com.anshu.imageservice.model.ImageMetadata;
import com.anshu.imageservice.repository.ImageMetadataRepository;
import com.anshu.imageservice.repository.ImageRepository;
import com.anshu.imageservice.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
public class ImageProcessor {

    private final ImageRepository imageRepository;
    private final ImageMetadataRepository metadataRepository;
    private final StorageService storageService;

    public ImageProcessor(ImageRepository imageRepository,
                          ImageMetadataRepository metadataRepository,
                          StorageService storageService) {
        this.imageRepository = imageRepository;
        this.metadataRepository = metadataRepository;
        this.storageService = storageService;
    }

    @Transactional
    public void process(ImageUploadedEvent event) throws IOException {

        if (imageRepository.existsByUuid(event.imageUuid())) {
            log.warn("[SKIP] Already processed {}", event.imageUuid());
            return;
        }

        Path tempPath = Paths.get(event.tempPath());
        byte[] bytes = Files.readAllBytes(tempPath);

        String imageUrl =
                storageService.store(bytes, event.imageUuid().toString());

        imageRepository.save(
                Image.builder()
                        .uuid(event.imageUuid())
                        .deviceUuid(event.deviceUuid())
                        .status("SAVED")
                        .capturedAt(event.capturedAt())
                        .createdAt(event.capturedAt())
                        .build()
        );

        metadataRepository.save(
                ImageMetadata.builder()
                        .uuid(event.metadataUuid())
                        .imageUuid(event.imageUuid())
                        .contentType("image/jpeg")
                        .fileSize((long) bytes.length)
                        .imageUrl(imageUrl)
                        .createdAt(event.capturedAt())
                        .build()
        );

        Files.deleteIfExists(tempPath);

        log.info("[DONE] Image processed {}", event.imageUuid());
    }
}
