package com.anshu.imageservice.core.application;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.anshu.imageservice.core.api.v1.dto.DeviceImageSummaryResponse;
import com.anshu.imageservice.core.api.v1.dto.PaginatedDeviceImageSummaryResponse;
import com.anshu.imageservice.core.domain.repository.DeviceSummaryRepository;
import com.anshu.imageservice.security.UserAccessValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeviceImageSummaryApplicationService {

    private final DeviceSummaryRepository repository;
    private final UserAccessValidator accessValidator;

    public PaginatedDeviceImageSummaryResponse getDeviceImageSummaries(
            UUID userUuid,
            String role,
            Pageable pageable
    ) {

        // ✅ Validate admin access
        accessValidator.validateAdmin(userUuid, role);

        var pageResult = repository.findAll(pageable);

        var content = pageResult.getContent()
                .stream()
                .map(ds -> DeviceImageSummaryResponse.builder()
                        .deviceUuid(ds.getDeviceUuid())
                        .deviceName(ds.getDeviceName())
                        .totalImages(ds.getTotalImages())
                        .activeImages(ds.getActiveImages())
                        .lastImageAt(ds.getLastImageAt())
                        .latestImageUuid(ds.getLatestImageUuid())
                        .build()
                )
                .toList();

        return PaginatedDeviceImageSummaryResponse.builder()
                .content(content)
                .page(pageResult.getNumber())
                .size(pageResult.getSize())
                .totalElements(pageResult.getTotalElements())
                .totalPages(pageResult.getTotalPages())
                .last(pageResult.isLast())
                .build();
    }
}
