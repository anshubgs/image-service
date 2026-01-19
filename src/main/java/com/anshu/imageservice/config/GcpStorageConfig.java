package com.anshu.imageservice.config;

//import com.google.cloud.storage.Storage;
//import com.google.cloud.storage.StorageOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("gcp")
public class GcpStorageConfig {

//    @Bean
//    public Storage storage() {
//        return StorageOptions.getDefaultInstance().getService();
//    }
}
