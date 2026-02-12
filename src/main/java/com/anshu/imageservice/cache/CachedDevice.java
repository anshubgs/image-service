package com.anshu.imageservice.cache;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CachedDevice implements Serializable {

    private UUID deviceUuid;
    private String deviceName;
    private String deviceType;
    private String status;
    private Instant registeredAt;
    private UUID userUuid;
}
