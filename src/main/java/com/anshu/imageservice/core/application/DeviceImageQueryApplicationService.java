package com.anshu.imageservice.core.application;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.anshu.imageservice.core.api.v1.dto.DeviceImageResponse;
import com.anshu.imageservice.core.domain.repository.DeviceImageRepository;
import com.anshu.imageservice.model.Image;
import com.anshu.imageservice.repository.ImageMetadataRepository;
import com.anshu.imageservice.security.UserAccessValidator;
import com.anshu.imageservice.service.StorageService;
import com.anshu.imageservice.model.ImageMetadata;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeviceImageQueryApplicationService {

    private final DeviceImageRepository imageRepository;
    private final ImageMetadataRepository metadataRepository;
    private final UserAccessValidator accessValidator;
    private final StorageService storageService;

    // ================================
    // 1️⃣ Latest Images (Optimized)
    // ================================
    public List<DeviceImageResponse> getLatestImages(
            UUID deviceUuid,
            UUID userUuid,
            String role
    ) {

        accessValidator.validateAdmin(userUuid, role);

        List<Image> images =
                imageRepository.findTop5ByDeviceUuid(deviceUuid);

        if (images.isEmpty()) {
            return List.of();
        }

        return mapImagesToResponse(images);
    }

    // =================================
    // 2️⃣ Images By Date (Optimized)
    // =================================
    public Page<DeviceImageResponse> getImagesByDate(
            UUID deviceUuid,
            LocalDate date,
            Pageable pageable,
            UUID userUuid,
            String role
    ) {
    	
    	 accessValidator.validateAdmin(userUuid, role);

        LocalDateTime start;
        LocalDateTime end;

        if (date == null) {
            end = LocalDateTime.now();
            start = end.minusDays(7);
        } else {
            start = date.atStartOfDay();
            end = date.plusDays(1).atStartOfDay();

        }

        Page<Image> imagePage =
                imageRepository.findByDeviceAndDateRange(
                        deviceUuid,
                        start,
                        end,
                        pageable
                );

        if (imagePage.isEmpty()) {
            return new PageImpl<>(List.of(), pageable, 0);
        }


        List<DeviceImageResponse> responses =
                mapImagesToResponse(imagePage.getContent());

        return new PageImpl<>(
                responses,
                pageable,
                imagePage.getTotalElements()
        );
    }

    // =================================
    // 🔥 Common Mapping Method
    // =================================
    private List<DeviceImageResponse> mapImagesToResponse(
            List<Image> images
    ) {

        List<UUID> imageUuids = images.stream()
                .map(Image::getUuid)
                .toList();

        List<ImageMetadata> metadataList =
                metadataRepository.findByImageUuidIn(imageUuids);

        // O(1) lookup
        Map<UUID, ImageMetadata> metadataMap =
                metadataList.stream()
                        .collect(Collectors.toMap(
                                ImageMetadata::getImageUuid,
                                m -> m
                        ));

        return images.stream()
                .map(image -> {

                    ImageMetadata meta =
                            metadataMap.get(image.getUuid());

                    String signedUrl = null;

                    if (meta != null && meta.getImageUrl() != null) {
                        signedUrl =
                                storageService.generateSignedUrl(
                                        meta.getImageUrl()
                                );
                    }

                    return DeviceImageResponse.builder()
                            .imageUuid(image.getUuid())
                            .imageUrl(signedUrl)
                            .capturedAt(image.getCapturedAt())
                            .build();
                })
                .toList();
    }
}
