package com.anshu.imageservice.event;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Immutable domain event
 */
public record ImageUploadedEvent(
        UUID imageUuid,
        UUID metadataUuid,
        UUID deviceUuid,
       String tempPath,

        // byte[] imageBytes,
       LocalDateTime capturedAt

) {
}
