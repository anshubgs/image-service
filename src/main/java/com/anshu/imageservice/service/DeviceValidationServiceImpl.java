package com.anshu.imageservice.service;

import com.anshu.imageservice.exception.DeviceInactiveException;
import com.anshu.imageservice.exception.DeviceNotFoundException;
import com.anshu.imageservice.exception.InvalidDeviceSecretException;
import com.anshu.imageservice.model.DeviceCache;
import com.anshu.imageservice.repository.DeviceCacheRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeviceValidationServiceImpl implements DeviceValidationService{

    private final DeviceCacheRepository repo;

    public DeviceValidationServiceImpl(DeviceCacheRepository repo){
        this.repo = repo;
    }

    @Override
    public void validateDevice(UUID deviceUuid, String deviceSecret) {

        DeviceCache device = repo.findByUuid(deviceUuid)
                .orElseThrow(() -> new DeviceNotFoundException("Device not registered"));

        if (deviceSecret == null ||
                !device.getDeviceSecret().equals(deviceSecret)) {
            throw new InvalidDeviceSecretException("Invalid device secret");
        }
        validateStatus(device);


    }

    @Override
    public void validateDevice(UUID deviceUuid) {

        DeviceCache device = repo.findByUuid(deviceUuid)
                .orElseThrow(() -> new DeviceNotFoundException("Device not registered"));

        validateStatus(device);
    }

    private void validateStatus(DeviceCache device) {
        if(!"ACTIVE".equalsIgnoreCase(device.getStatus())){
            throw new DeviceInactiveException("Device not active");
        }
    }
}
