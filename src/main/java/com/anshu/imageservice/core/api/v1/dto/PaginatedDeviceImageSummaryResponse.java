package com.anshu.imageservice.core.api.v1.dto;

import lombok.Builder;
import java.util.List;

@Builder
public record PaginatedDeviceImageSummaryResponse(
        List<DeviceImageSummaryResponse> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean last
) {}
