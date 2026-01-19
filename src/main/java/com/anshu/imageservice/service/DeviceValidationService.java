package com.anshu.imageservice.service;

import java.util.UUID;

public interface DeviceValidationService {
    void validateDevice(UUID deviceUuid, String deviceSecret);
}
