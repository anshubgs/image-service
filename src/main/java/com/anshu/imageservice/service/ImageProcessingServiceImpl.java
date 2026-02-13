package com.anshu.imageservice.service;

import com.google.cloud.storage.Storage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ImageProcessingServiceImpl implements ImageProcessingService {

    private final Storage storage;

    @Value("${gcs.bucket.name}")
    private String bucketName;

    public ImageProcessingServiceImpl(Storage storage) {
        this.storage = storage;
    }

    @Override
    public void process(String objectPath) {

        try {
            byte[] imageBytes =
                    storage.readAllBytes(bucketName, objectPath);

            log.info("[PROCESS] Image loaded from GCS | object={}", objectPath);

            // TODO: resize / thumbnail / ML processing

        } catch (Exception e) {
            log.error("[PROCESS] Image processing failed", e);
            throw new RuntimeException("Image processing failed", e);
        }
    }
}
