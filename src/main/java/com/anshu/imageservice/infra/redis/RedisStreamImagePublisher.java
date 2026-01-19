package com.anshu.imageservice.infra.redis;

import com.anshu.imageservice.event.ImageEventPublisher;
import com.anshu.imageservice.event.ImageUploadedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.stereotype.Service;

import java.util.Map;

//@Primary
@Slf4j
@Service
public class RedisStreamImagePublisher implements ImageEventPublisher {

    private static final String STREAM = "image.upload.stream";

    private final StreamOperations<String, String, String> streamOps;

    public RedisStreamImagePublisher(RedisTemplate<String, String> redisTemplate) {
        this.streamOps = redisTemplate.opsForStream();
    }

    @Override
    public void publish(ImageUploadedEvent event) {

        streamOps.add(STREAM, Map.of(
                "imageUuid", event.imageUuid().toString(),
                "metadataUuid", event.metadataUuid().toString(),
                "deviceUuid", event.deviceUuid().toString(),
                "tempPath", event.tempPath(),
                "capturedAt", event.capturedAt().toString()
        ));

        log.info("[REDIS-PUBLISH] imageUuid={}", event.imageUuid());
    }
}
