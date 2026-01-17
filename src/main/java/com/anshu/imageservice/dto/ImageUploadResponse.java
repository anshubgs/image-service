package com.anshu.imageservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageUploadResponse {

    private UUID imageMetadataUuid;
    private String status;
    private LocalDateTime createdAt;
}
