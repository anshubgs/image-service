package com.anshu.imageservice.service;

import com.anshu.imageservice.service.StorageService;
//import com.google.cloud.storage.BlobInfo;
//import com.google.cloud.storage.Storage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Profile("gcp")   // gcp profile pe activate hoga
@Slf4j
public class GcpStorageServiceImpl {

   /* private final Storage storage;

    @Value("${gcp.bucket.name}")
    private String bucketName;

    public GcpStorageServiceImpl(Storage storage) {
        this.storage = storage;
    }

    @Override
    public String store(MultipartFile file, String imageUuid) {

        try {
            String objectName = "images/" + imageUuid + ".jpg";

            BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, objectName)
                    .setContentType(file.getContentType())
                    .build();

            storage.create(blobInfo, file.getBytes());

            String publicUrl =
                    "https://storage.googleapis.com/" + bucketName + "/" + objectName;

            log.info("Image uploaded to GCP bucket {}", publicUrl);

            return publicUrl;

        } catch (Exception e) {
            log.error("GCP image upload failed", e);
            throw new RuntimeException("Failed to upload image to GCP");
        }
    }*/
}
