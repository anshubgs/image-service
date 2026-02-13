package com.anshu.imageservice.controller;

import com.anshu.imageservice.dto.ImageResponse;
import com.anshu.imageservice.service.ImageQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/images")
public class ImageQueryController {

    private final ImageQueryService queryService;

    public ImageQueryController(ImageQueryService queryService) {
        this.queryService = queryService;
    }

    // 🔹 Get image by imageUuid
    @GetMapping("/{imageUuid}")
    public ResponseEntity<ImageResponse> getImage(
            @PathVariable UUID imageUuid) {

        ImageResponse response = queryService.getImage(imageUuid);
        return ResponseEntity.ok(response);
    }

    // 🔹 Timeline (list images by deviceUuid)
    @GetMapping
    public ResponseEntity<List<ImageResponse>> listImages(
            @RequestParam UUID deviceUuid,
            Pageable pageable) {

        List<ImageResponse> response =
                queryService.listImages(deviceUuid, pageable);
        return ResponseEntity.ok(response);
    }

    // 🔹 Latest image by device
    @GetMapping("/latest/{deviceUuid}")
    public ResponseEntity<ImageResponse> latest(
            @PathVariable UUID deviceUuid) {

        ImageResponse response =
                queryService.getLatestImage(deviceUuid);
        return ResponseEntity.ok(response);
    }
}
