package com.anshu.imageservice.infra.redis;

import com.anshu.imageservice.event.ImageUploadedEvent;
import com.anshu.imageservice.processor.ImageProcessor;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
public class ImageStreamConsumer {

    private static final String STREAM = "image.upload.stream";
    private static final String GROUP = "image-workers";
    private static final String CONSUMER = "worker-1";
    private static final String DLQ = "image.upload.dlq";

    private final StreamOperations<String, String, String> streamOps;
    private final ImageProcessor processor;

    public ImageStreamConsumer( @Qualifier("redisTemplate")RedisTemplate<String, String> template,
                               ImageProcessor processor) {
        this.streamOps = template.opsForStream();
        this.processor = processor;
    }

    @PostConstruct
    void initGroup() {
        try {
            streamOps.createGroup(STREAM, ReadOffset.latest(), GROUP);
            log.info("[REDIS] Consumer group created");
        } catch (Exception e) {
            log.info("[REDIS] Consumer group already exists");
        }
    }

    @Scheduled(fixedDelay = 1000)
    public void consume() {

        List<MapRecord<String, String, String>> records =
                streamOps.read(
                        Consumer.from(GROUP, CONSUMER),
                        StreamReadOptions.empty()
                                .count(1)
                                .block(Duration.ofSeconds(5)),
                        StreamOffset.create(STREAM, ReadOffset.lastConsumed())
                );

        if (records == null || records.isEmpty()) {
            return;
        }

        for (MapRecord<String, String, String> record : records) {
            try {
                Map<String, String> data = record.getValue();

                ImageUploadedEvent event = new ImageUploadedEvent(
                        UUID.fromString(data.get("imageUuid")),
                        UUID.fromString(data.get("metadataUuid")),
                        UUID.fromString(data.get("deviceUuid")),
                        data.get("tempPath"),
                        LocalDateTime.parse(data.get("capturedAt"))
                );

                log.info("[REDIS-CONSUME] Processing imageUuid={}", event.imageUuid());

                processor.process(event);

                streamOps.acknowledge(STREAM, GROUP, record.getId());
                log.info("[ACK] imageUuid={}", event.imageUuid());

            } catch (Exception ex) {
                log.error("[DLQ] Processing failed", ex);

                streamOps.add(DLQ, record.getValue());
                streamOps.acknowledge(STREAM, GROUP, record.getId());
            }
        }
    }
}
