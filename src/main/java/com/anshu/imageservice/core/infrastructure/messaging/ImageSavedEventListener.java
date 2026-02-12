package com.anshu.imageservice.core.infrastructure.messaging;

import com.anshu.imageservice.core.infrastructure.repository.DeviceSummaryJpaRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImageSavedEventListener
        implements StreamListener<String, MapRecord<String, String, String>> {

    private final DeviceSummaryJpaRepository deviceSummaryRepository;

    @Transactional
    @Override
    public void onMessage(MapRecord<String, String, String> message) {

        log.info("🔥 [REDIS CONSUMER HIT] Raw Message => {}", message);

        try {
            String imageStr = message.getValue().get("imageUuid");
            String deviceStr = message.getValue().get("deviceUuid");
            String capturedStr = message.getValue().get("capturedAt");
            //String deviceName = message.getValue().get("deviceName");

            String deviceName = message.getValue().get("deviceName"); // ✅ add this

            log.info("📦 Extracted Fields | imageUuid={} | deviceUuid={} | capturedAt={} | deviceName={}",
                    imageStr, deviceStr, capturedStr, deviceName);
            
            UUID imageUuid = UUID.fromString(imageStr);
            UUID deviceUuid = UUID.fromString(deviceStr);
            LocalDateTime capturedAt = LocalDateTime.parse(capturedStr);

            log.info("✅ Parsed Successfully | device={}", deviceUuid);

            log.info("🗄️ Calling DB UPSERT...");
            deviceSummaryRepository.upsert(deviceUuid, imageUuid, capturedAt, deviceName);

            log.info("✅ [SUMMARY UPSERTED SUCCESS] device={}", deviceUuid);

        } catch (Exception e) {
            log.error("❌ [LISTENER ERROR] Failed to process redis message", e);
        }
    }
}
