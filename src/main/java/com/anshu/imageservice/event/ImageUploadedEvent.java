package com.anshu.imageservice.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record ImageUploadedEvent(
        UUID imageUuid,
        UUID metadataUuid,
        UUID deviceUuid,
        String objectPath,   // 🔥 GCS object path
        LocalDateTime capturedAt
) {
}
