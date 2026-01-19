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

        if(!device.getDeviceSecret().equals(deviceSecret)){
            throw new InvalidDeviceSecretException("Invalid device secret");
        }

        if(!"ACTIVE".equalsIgnoreCase(device.getStatus())){
            throw new DeviceInactiveException("Device not active");
        }
    }
}
