package com.anshu.imageservice.storage.service;

import com.anshu.imageservice.model.CachedUser;
import com.anshu.imageservice.model.ImageMetadata;
import com.anshu.imageservice.repository.CachedUserRepository;
import com.anshu.imageservice.repository.ImageMetadataRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LocalImageAccessService {

    private final CachedUserRepository cachedUserRepository;
    private final ImageMetadataRepository imageMetadataRepository;

    @Value("${server.port}")
    private String port;

    public String getLatestImageUrl(
            UUID headerUserUuid,
            String headerUserRole,
            UUID deviceUuid
    ) {

        // 🔐 user validation (simple)
        CachedUser user = cachedUserRepository
                .findByUuid(headerUserUuid)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        if (!user.getRole().equalsIgnoreCase(headerUserRole)) {
            throw new RuntimeException("Role mismatch");
        }

        // 🖼️ latest image from metadata
        ImageMetadata meta = imageMetadataRepository
                .findTopByDeviceUuidOrderByCreatedAtDesc(deviceUuid)
                .orElseThrow(() ->
                        new RuntimeException("No image found for device"));

        UUID imageUuid = meta.getImageUuid();

        // 🔗 LOCAL IMAGE URL
        return "http://localhost:" + port
                + "/api/v1/image-access/images/"
                + deviceUuid + "/"
                + imageUuid + ".jpg";
    }
}
