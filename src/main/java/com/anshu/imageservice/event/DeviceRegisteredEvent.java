package com.anshu.imageservice.event;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class DeviceRegisteredEvent {
    private UUID deviceUuid;
    private String deviceName;
    private String deviceType;
    private String deviceSecret;
    private String status;
    private Instant registeredAt;
}
