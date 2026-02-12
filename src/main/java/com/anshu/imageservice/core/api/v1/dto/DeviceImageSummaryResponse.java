package com.anshu.imageservice.core.api.v1.dto;

import lombok.Builder;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record DeviceImageSummaryResponse(
        UUID deviceUuid,
        String deviceName,
        int totalImages,
        int activeImages,
        LocalDateTime lastImageAt,
        UUID latestImageUuid
) {}
