package com.anshu.imageservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageViewResponse {

    private UUID imageMetadataUuid;
    private UUID imageUuid;
    private UUID deviceUuid;

    private String thumbnailUrl;
    private String imageUrl;

    private LocalDateTime capturedAt;
}
