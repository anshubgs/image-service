package com.anshu.imageservice.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record ImageSavedEvent(
        UUID imageUuid,
        UUID deviceUuid,
        LocalDateTime capturedAt,
        String deviceName
) {}
