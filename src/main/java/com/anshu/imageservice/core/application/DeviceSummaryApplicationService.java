package com.anshu.imageservice.core.application;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anshu.imageservice.core.infrastructure.entity.DeviceSummary;
import com.anshu.imageservice.core.infrastructure.repository.DeviceSummaryJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeviceSummaryApplicationService {

//    private final DeviceSummaryJpaRepository repository;
//
//    @Transactional
//    public void handleImageUploaded(UUID deviceUuid, UUID imageUuid, LocalDateTime capturedAt) {
//
//        // Try to update existing device summary
//        int updated = repository.increment(deviceUuid, imageUuid, capturedAt);
//
//        // If no rows were updated, it's the first image → insert a new summary
//        if (updated == 0) {
//            DeviceSummary summary = DeviceSummary.builder()
//                    .uuid(UUID.randomUUID())
//                    .deviceUuid(deviceUuid)
//                    .totalImages(1)
//                    .activeImages(1)
//                    .lastImageAt(capturedAt)
//                    .latestImageUuid(imageUuid)
//                    .updatedAt(LocalDateTime.now())
//                    .build();
//
//            repository.save(summary);
//        }
//    }
}
