package com.anshu.imageservice.service;

import com.google.cloud.storage.BlobId;
import java.net.URL;
import java.util.concurrent.TimeUnit;

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
    public String storeLatest(byte[] imageBytes, UUID deviceUuid) {

        try {

            String objectName =
                    basePath + "/" + deviceUuid + "/latest.jpg";

            BlobId blobId = BlobId.of(bucketName, objectName);

            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType("image/jpeg")
                    .build();

            storage.create(blobInfo, imageBytes); // overwrite automatically

            log.info("[GCS] latest.jpg overwritten | bucket={} | object={}",
                    bucketName, objectName);

            return objectName;

        } catch (Exception e) {
            log.error("[GCS] latest.jpg upload failed", e);
            throw new RuntimeException("Failed to upload image to GCS");
        }
    }

    @Override
    public String generateSignedUrl(String objectPath) {

        try {

            BlobInfo blobInfo = BlobInfo.newBuilder(
                    BlobId.of(bucketName, objectPath)
            ).build();

            URL signedUrl = storage.signUrl(
                    blobInfo,
                    15, // validity
                    TimeUnit.MINUTES,
                    Storage.SignUrlOption.withV4Signature()
            );

            return signedUrl.toString();

        } catch (Exception e) {
            log.error("[GCS] Failed to generate signed URL", e);
            throw new RuntimeException("Failed to generate signed URL");
        }
    }


}
