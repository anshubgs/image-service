package com.anshu.imageservice.service;

import com.anshu.imageservice.client.DeviceServiceClient;
import com.anshu.imageservice.core.infrastructure.messaging.ImageSavedEventPublisher;
import com.anshu.imageservice.core.infrastructure.repository.DeviceSummaryJpaRepository;
import com.anshu.imageservice.dto.ImageUploadResponse;
import com.anshu.imageservice.event.ImageEventPublisher;
import com.anshu.imageservice.event.ImageSavedEvent;
import com.anshu.imageservice.event.ImageUploadedEvent;
import com.anshu.imageservice.model.DeviceCache;
import com.anshu.imageservice.model.Image;
import com.anshu.imageservice.model.ImageMetadata;
import com.anshu.imageservice.repository.DeviceCacheRepository;
import com.anshu.imageservice.repository.ImageMetadataRepository;
import com.anshu.imageservice.repository.ImageRepository;

import org.springframework.transaction.annotation.Transactional;

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
   // private final ImageEventPublisher eventPublisher;
    private final StorageService storageService;
    private final ImageMetadataRepository imageMetadataRepository;
    private final ImageRepository imageRepository;
    private final ImageSavedEventPublisher eventPublisher;
    private final DeviceSummaryJpaRepository jpaRepo;
    private final DeviceCacheRepository cacheRepo;


    /*private final ImageRepository imageRepository;
    private final ImageMetadataRepository metadataRepository;
    private final StorageService storageService;*/

    public ImageServiceImpl(DeviceValidationService deviceValidationService,
    		ImageSavedEventPublisher eventPublisher,DeviceServiceClient serviceClient,StorageService storageService,
                          ImageMetadataRepository imageMetadataRepository,ImageRepository imageRepository,DeviceSummaryJpaRepository jpaRepo,
                          DeviceCacheRepository cacheRepo) {
        this.deviceValidationService = deviceValidationService;
        this.eventPublisher = eventPublisher;
        this.serviceClient = serviceClient;
        this.storageService = storageService;
        this.imageMetadataRepository = imageMetadataRepository;
        this.imageRepository = imageRepository;
        this.jpaRepo = jpaRepo;
        this.cacheRepo = cacheRepo;
    }


    @Override
    @Transactional
    public ImageUploadResponse uploadImage(
            UUID deviceUuid,
            String deviceSecret,
            byte[] imageBytes
    ) {

        // 1️⃣ Validate device
        serviceClient.validateDevice(deviceUuid, deviceSecret);

        UUID imageUuid = UUID.randomUUID();
        UUID metadataUuid = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
      //  String 

        // 2️⃣ Upload to GCS
        String gcsPath =
                storageService.storeLatest(imageBytes, deviceUuid);


        log.info("[GCS] Image uploaded | image={} | path={}", imageUuid, gcsPath);

        // 3️⃣ INSERT INTO IMAGE (PARENT)
        Image image = Image.builder()
                .uuid(imageUuid)
                .deviceUuid(deviceUuid)
                .status("UPLOADED")
                .capturedAt(now)
                .createdAt(now)
                .updatedAt(now)
                .build();

        imageRepository.save(image);

        log.info("[IMAGE] Image row inserted | image={}", imageUuid);

        // 4️⃣ INSERT INTO IMAGE_METADATA (CHILD)
        ImageMetadata metadata = ImageMetadata.builder()
                .uuid(metadataUuid)
                .imageUuid(imageUuid)
                .deviceUuid(deviceUuid)
                .originalFilename(imageUuid + ".jpg")
                .contentType("image/jpeg")
                .fileSize((long) imageBytes.length)
                .imageUrl(gcsPath)     // ✅ CORRECT
                .thumbnailUrl(null)    // async later
                .createdAt(now)
                .build();

        imageMetadataRepository.save(metadata);

        log.info("[METADATA] Saved | image={} | device={}", imageUuid, deviceUuid);

        // 5️⃣ Publish event
//        eventPublisher.publish(new ImageUploadedEvent(
//                imageUuid,
//                metadataUuid,
//                deviceUuid,
//                gcsPath,
//                now
//        ));
        
        
        //Get Device Name
        DeviceCache cache = cacheRepo.findByUuid(deviceUuid).orElseThrow(() -> new RuntimeException("Device not found in cache"));
        String deviceName = cache.getDeviceName();
        
        
        try {
        	eventPublisher.publish(
                new ImageSavedEvent(imageUuid, deviceUuid, now, deviceName,  gcsPath )
            );
        } catch (Exception ex) {
            log.error("Redis down. Falling back to direct DB update");

            jpaRepo.upsert(
                deviceUuid,
                imageUuid,
                now,
                deviceName,
                gcsPath 
            );
        }

        return ImageUploadResponse.builder()
                .imageMetadataUuid(metadataUuid)
                .status("UPLOADED")
                .createdAt(now)
                .build();
    }

}
