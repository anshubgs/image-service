package com.anshu.imageservice.core.infrastructure.messaging;

import com.anshu.imageservice.event.ImageSavedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisImageSavedEventPublisher implements ImageSavedEventPublisher {

    private final StringRedisTemplate redisTemplate;

    private static final String STREAM = "image.saved.stream";

    @Override
    public void publish(ImageSavedEvent event) {

        var recordId = redisTemplate.opsForStream().add(
                STREAM,
                Map.of(
                        "imageUuid", event.imageUuid().toString(),
                        "deviceUuid", event.deviceUuid().toString(),
                        "capturedAt", event.capturedAt().toString(),
                        "deviceName", event.deviceName().toString()
                )
        );

        log.info("✅ Published to Redis stream {} id={}", STREAM, recordId);
    }
}
