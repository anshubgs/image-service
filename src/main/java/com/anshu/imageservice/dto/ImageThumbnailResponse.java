package com.anshu.imageservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageThumbnailResponse {

    private UUID imageMetadataUuid;
    private String thumbnailUrl;
    private LocalDateTime capturedAt;
}
