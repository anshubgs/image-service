package com.anshu.imageservice.listener;

import com.anshu.imageservice.cache.CachedDevice;
import com.anshu.imageservice.event.DeviceRegisteredEvent;
import com.anshu.imageservice.model.DeviceCache;
import com.anshu.imageservice.repository.DeviceCacheRepository;
import com.anshu.imageservice.service.DeviceCacheService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage;
import com.google.cloud.spring.pubsub.support.GcpPubSubHeaders;

import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceRegisteredEventListener {

    private final ObjectMapper objectMapper;
    private final DeviceCacheService deviceCacheService;
    private final DeviceCacheRepository deviceCacheRepository;

    @Value("${pubsub.subscription.device-registered}")
    private String subscription;

    @ServiceActivator(inputChannel = "pubsubInputChannel")
    public void listen(
            String payload,
            @Header(GcpPubSubHeaders.ORIGINAL_MESSAGE)
            BasicAcknowledgeablePubsubMessage message) {

        try {
            // 🔥 FULL JSON LOG
            log.info("📥 Received DeviceRegisteredEvent | payload={}", payload);

            DeviceRegisteredEvent event =
                    objectMapper.readValue(payload, DeviceRegisteredEvent.class);

            //SAVE TO REDIS
            CachedDevice cachedDevice = CachedDevice.builder()
                    .deviceUuid(event.getDeviceUuid())
                    .deviceName(event.getDeviceName())
                    .deviceType(event.getDeviceType())
                    .status(event.getStatus())
                    .registeredAt(event.getRegisteredAt())
                    .build();

            deviceCacheService.save(cachedDevice);

            //SAVE / UPDATE DB
            DeviceCache deviceCache = deviceCacheRepository.findByUuid(
                    event.getDeviceUuid()).orElse(DeviceCache.builder()
                            .uuid(event.getDeviceUuid())
                            .createdAt(LocalDateTime.now())
                    .build());
            deviceCache.setDeviceName(event.getDeviceName());
            deviceCache.setDeviceType(event.getDeviceType());
            deviceCache.setStatus(event.getStatus());
            deviceCache.setDeviceSecret(event.getDeviceSecret());
            deviceCache.setLastSyncedAt(LocalDateTime.now());

            deviceCacheRepository.save(deviceCache);

            log.info(
                    "🗄 Device cached in Redis & DB | uuid={}",
                    event.getDeviceUuid()
            );

            // ✅ ACK
            message.ack();
            log.info("✅ Event processed & acknowledged");

        } catch (Exception e) {
            log.error("❌ Error processing DeviceRegisteredEvent", e);
            message.nack();
        }
    }
}
