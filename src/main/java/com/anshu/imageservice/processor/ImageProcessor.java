package com.anshu.imageservice.processor;


import com.anshu.imageservice.core.infrastructure.entity.DeviceSummary;
import com.anshu.imageservice.core.infrastructure.messaging.ImageSavedEventPublisher;
import com.anshu.imageservice.core.infrastructure.repository.DeviceSummaryJpaRepository;
import com.anshu.imageservice.event.ImageSavedEvent;
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
    private final ImageSavedEventPublisher eventPublisher;
    private final DeviceSummaryJpaRepository jpaRepo;


    public ImageProcessor(ImageRepository imageRepository,
                          ImageMetadataRepository metadataRepository,
                          StorageService storageService,ImageSavedEventPublisher eventPublisher,DeviceSummaryJpaRepository jpaRepo) {
        this.imageRepository = imageRepository;
        this.metadataRepository = metadataRepository;
        this.storageService = storageService;
        this.eventPublisher = eventPublisher;
        this.jpaRepo = jpaRepo;
    }

    @Transactional
    public void process(ImageUploadedEvent event) throws IOException {

//        if (imageRepository.existsByUuid(event.imageUuid())) {
//            log.warn("[SKIP] Already processed {}", event.imageUuid());
//            return;
//        }

        // ✅ Instead of local temp file, use objectPath (GCS path)
        String objectPath = event.objectPath();

        // If you want bytes (optional, GCS storageService might provide method to download)
        // byte[] bytes = storageService.download(objectPath); // implement this if needed

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
                        .fileSize(null) // optional: fetch from GCS if needed
                        .imageUrl(objectPath) // GCS object path
                        .createdAt(event.capturedAt())
                        .build()
        );
        
//        jpaRepo.upsert(
//                event.deviceUuid(),
//                event.imageUuid(),
//                event.capturedAt()
//        );

//        eventPublisher.publish(
//                new ImageSavedEvent(
//                        event.imageUuid(),
//                        event.deviceUuid(),
//                        event.capturedAt()
//                )
//        );


        log.info("[DONE] Image processed {}", event.imageUuid());
    }

}
