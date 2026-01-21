package com.anshu.imageservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Jacksonized       // <--- Important for Jackson + Lombok builder
public class ImageResponse {
    private UUID imageUuid;
    private UUID deviceUuid;
    private String status;
    private String imageUrl;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime capturedAt;


}
