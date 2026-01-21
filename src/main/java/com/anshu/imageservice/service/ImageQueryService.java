package com.anshu.imageservice.service;

import com.anshu.imageservice.dto.ImageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ImageQueryService {

    ImageResponse getImage(UUID imageUuid);

    ImageResponse getLatestImage(UUID deviceUuid);

    List<ImageResponse> listImages(UUID deviceUuid, Pageable pageable);
}
