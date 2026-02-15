package com.anshu.imageservice.core.api.v1.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;

@Builder
public record DeviceImageResponse(
        UUID imageUuid,
        String imageUrl,
        LocalDateTime capturedAt
) {}
