package com.anshu.imageservice.service;

import com.google.cloud.storage.BlobId;
import java.util.UUID;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GcpStorageServiceImpl implements StorageService {

    private final Storage storage;

    @Value("${gcs.bucket.name}")
    private String bucketName;

    @Value("${gcs.object.base-path:devices}")
    private String basePath;

    public GcpStorageServiceImpl(Storage storage) {
        this.storage = storage;
    }

    @Override
    public String store(byte[] imageBytes, UUID deviceUuid, UUID imageUuid) {

        try {
            String objectName =
                    basePath + "/" + deviceUuid + "/" + imageUuid + ".jpg";

            BlobId blobId = BlobId.of(bucketName, objectName);

            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType("image/jpeg")
                    .build();

            storage.create(blobInfo, imageBytes);

            log.info("[GCS] Image uploaded | bucket={} | object={}",
                    bucketName, objectName);

            // ✅ RETURN ONLY OBJECT PATH
            return objectName;

        } catch (Exception e) {
            log.error("[GCS] Image upload failed", e);
            throw new RuntimeException("Failed to upload image to GCS");
        }
    }

}
