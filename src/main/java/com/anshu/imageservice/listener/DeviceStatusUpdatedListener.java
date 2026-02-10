package com.anshu.imageservice.listener;

import com.anshu.imageservice.event.DeviceStatusUpdatedEvent;
import com.anshu.imageservice.model.DeviceCache;
import com.anshu.imageservice.repository.DeviceCacheRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.spring.pubsub.support.AcknowledgeablePubsubMessage;
import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage;
import com.google.cloud.spring.pubsub.support.GcpPubSubHeaders;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Service;
import org.springframework.messaging.handler.annotation.Header;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeviceStatusUpdatedListener {

    private final ObjectMapper objectMapper;
    private final DeviceCacheRepository deviceCacheRepository;

    @ServiceActivator(inputChannel = "deviceStatusUpdatedInputChannel")
    public void listen(
            String payload,
            @Header(GcpPubSubHeaders.ORIGINAL_MESSAGE)
            BasicAcknowledgeablePubsubMessage message) {

        try {
            log.info("📥 DeviceStatusUpdated event received: {}", payload);

            DeviceStatusUpdatedEvent event =
                    objectMapper.readValue(payload, DeviceStatusUpdatedEvent.class);

            UUID deviceUuid = UUID.fromString(event.getDeviceUuid());

            deviceCacheRepository.findByUuid(deviceUuid)
                    .ifPresent(deviceCache -> {
                        deviceCache.setStatus(event.getNewStatus());
                        deviceCacheRepository.save(deviceCache);

                        log.info("✅ Status updated | device={} | status={}",
                                deviceUuid, event.getNewStatus());
                    });

            message.ack();

        } catch (Exception e) {
            log.error("❌ Error processing DeviceStatusUpdated event", e);
            message.nack();
        }
    }
}
