package com.anshu.imageservice.storage.service;

import com.anshu.imageservice.model.CachedUser;
import com.anshu.imageservice.model.ImageMetadata;
import com.anshu.imageservice.repository.CachedUserRepository;
import com.anshu.imageservice.repository.ImageMetadataRepository;
import com.google.cloud.storage.*;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CloudImageAccessService implements ImageAccessService {

    private final Storage storage;
    private final CachedUserRepository cachedUserRepository;
    private final ImageMetadataRepository imageMetadataRepository;

    @Value("${gcs.bucket.name}")
    private String bucket;

    @Override
    public String getLatestImageUrl(
            UUID headerUserUuid,
            String headerUserRole,
            UUID deviceUuid
    ) {

        // 🔐 1. USER VALIDATION
        CachedUser cachedUser = cachedUserRepository
                .findByUuid(headerUserUuid)
                .orElseThrow(() ->
                        new RuntimeException("User not found in cache"));

        if (!cachedUser.getRole().equalsIgnoreCase(headerUserRole)) {
            throw new RuntimeException("User role mismatch");
        }

        // 🖼️ 2. GET LATEST METADATA
        ImageMetadata metadata = imageMetadataRepository
                .findTopByDeviceUuidOrderByCreatedAtDesc(deviceUuid)
                .orElseThrow(() ->
                        new RuntimeException("No image found for device"));

        // ✅ 3. USE STORED GCS PATH (NO GUESSING)
        // gs://bucket/devices/xxx.jpg
        String gcsUrl = metadata.getImageUrl();

        String objectName = gcsUrl
                .replace("gs://" + bucket + "/", "");

        BlobInfo blobInfo = BlobInfo.newBuilder(
                BlobId.of(bucket, objectName)
        ).build();

        // 🔗 4. SIGNED URL
        return storage.signUrl(
                blobInfo,
                15,
                TimeUnit.MINUTES,
                Storage.SignUrlOption.withV4Signature()
        ).toString();
    }
}
