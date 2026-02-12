package com.anshu.imageservice.security;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import com.anshu.imageservice.repository.CachedUserRepository;
import com.anshu.imageservice.repository.DeviceCacheRepository;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserAccessValidator {

    private final CachedUserRepository userRepository;
    private final DeviceCacheRepository deviceCacheRepository;
    

    public void validateAdmin(UUID userUuid, String role) {

        if (!"ADMIN".equalsIgnoreCase(role)) {
            throw new RuntimeException("Admin access required");
        }

        userRepository.findByUuid(userUuid)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void validateUserAccess(
            UUID userUuid,
            String role,
            UUID deviceUuid) {

        userRepository.findByUuid(userUuid)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if ("ADMIN".equalsIgnoreCase(role)) {
            return;
        }

//        boolean allowed =
//                deviceCacheRepository.existsByUser_UuidAndUuid(userUuid, deviceUuid);

//
//        if (!allowed) {
//            throw new RuntimeException("Access denied for this device");
//        }
    }
}

