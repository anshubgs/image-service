package com.anshu.imageservice.event;

import java.time.Instant;

import lombok.Data;

@Data
public class DeviceStatusUpdatedEvent {

    private String deviceUuid;
    private String oldStatus;
    private String newStatus;
    private String updatedBy;
    private Instant updatedAt;
}