package com.anshu.imageservice.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestClientException;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
public class DeviceServiceClient {

    private final RestClient restClient;

    public DeviceServiceClient(
            @Value("${device.service.url}") String baseUrl) {

        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public void validateDevice(UUID uuid, String secret){
        try{

            restClient.post()
                    .uri("api/v1/devices/validate")
                    .body(Map.of(
                            "deviceUuid",uuid,
                            "deviceSecret", secret
                    ))
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientException ex){
            log.error("[DEVICE VALIDATION FAILED] {}", ex.getMessage());
            throw ex;
        }

    }
}
