package com.anshu.imageservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageRangeResponse {

    private UUID deviceUuid;
    private LocalDateTime from;
    private LocalDateTime to;

    private List<ImageThumbnailResponse> images;
}
