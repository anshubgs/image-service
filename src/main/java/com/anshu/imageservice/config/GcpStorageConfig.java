package com.anshu.imageservice.config;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class GcpStorageConfig {

    @Bean
    public Storage storage(
            @Value("${spring.cloud.gcp.credentials.location}") String credentialsPath
    ) throws IOException {

        String path = credentialsPath.replace("file:", "");

        return StorageOptions.newBuilder()
                .setCredentials(
                        ServiceAccountCredentials.fromStream(
                                new FileInputStream(path)
                        )
                )
                .build()
                .getService();
    }
}
