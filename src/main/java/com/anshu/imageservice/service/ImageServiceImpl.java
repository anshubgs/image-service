package com.anshu.imageservice.service;

import com.anshu.imageservice.client.DeviceServiceClient;
import com.anshu.imageservice.dto.ImageUploadResponse;
import com.anshu.imageservice.event.ImageEventPublisher;
import com.anshu.imageservice.event.ImageUploadedEvent;
import com.anshu.imageservice.model.Image;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    private static final long MIN_FREE_SPACE =50*1024*1024;

    private final DeviceValidationService deviceValidationService;
    private final DeviceServiceClient serviceClient;
    private final ImageEventPublisher eventPublisher;
    /*private final ImageRepository imageRepository;
    private final ImageMetadataRepository metadataRepository;
    private final StorageService storageService;*/

    public ImageServiceImpl(DeviceValidationService deviceValidationService,
                          ImageEventPublisher eventPublisher,DeviceServiceClient serviceClient) {
        this.deviceValidationService = deviceValidationService;
        this.eventPublisher = eventPublisher;
        this.serviceClient = serviceClient;
    }


    @Override
    public ImageUploadResponse uploadImage(UUID deviceUuid,
                                           String deviceSecret,
                                           byte[] imageBytes) {

        // 1. Validate device
        //deviceValidationService.validateDevice(deviceUuid, deviceSecret);
        serviceClient.validateDevice(deviceUuid, deviceSecret);

        // Generate unique IDs for image and its metadata
        UUID imageUuid = UUID.randomUUID();
        UUID metadataUuid = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

        // Save image temporarily to disk
        try {
            // Create the directory if it doesn't exist
            Path tempDir = Paths.get("/tmp/images");
            // Check if there is enough free space in the temp directory
            Files.createDirectories(
                    tempDir
            );
            if (tempDir.toFile().getUsableSpace() < MIN_FREE_SPACE) {
                throw new RuntimeException("Temp storage full");
            }

            Path tempFile = tempDir.resolve(imageUuid + ".jpg");
            Files.write(tempFile, imageBytes);

            //publish Event
            eventPublisher.publish(new ImageUploadedEvent(
                    imageUuid, metadataUuid, deviceUuid, tempFile.toString(), now));

            log.info("[EVENT] Published {}", imageUuid);

            //return Response
            return ImageUploadResponse.builder()
                    .imageMetadataUuid(metadataUuid)
                    .status("RECEIVED")
                    .createdAt(now)
                    .build();

        } catch (IOException e) {
            log.error("[TEMP] Write failed", e);
            throw new RuntimeException("Image intake failed");
        }
    }
}
