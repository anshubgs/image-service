package com.anshu.imageservice.storage.controller;

import com.anshu.imageservice.storage.service.ImageAccessService;
import com.anshu.imageservice.storage.service.LocalImageAccessService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/image-access")
@RequiredArgsConstructor
public class ImageAccessController {

    private final ImageAccessService imageAccessService;

    @GetMapping("/latest")
    public Map<String, String> getLatestImage(

            @RequestHeader("X-USER-UUID") UUID userUuid,
            @RequestHeader("X-USER-ROLE") String userRole,
            @RequestParam UUID deviceUuid
    ) {
        return Map.of(
                "imageUrl",
                imageAccessService.getLatestImageUrl(
                        userUuid,
                        userRole,
                        deviceUuid
                )
        );
    }
}
