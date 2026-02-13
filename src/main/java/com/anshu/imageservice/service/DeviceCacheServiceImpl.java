package com.anshu.imageservice.service;

import com.anshu.imageservice.cache.CachedDevice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceCacheServiceImpl implements DeviceCacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String KEY_PREFIX = "device:";

    @Override
    public void save(CachedDevice device) {
        String key = KEY_PREFIX + device.getDeviceUuid();
        redisTemplate.opsForValue().set(key, device);
        log.info("🧠 Device cached in Redis | key={}", key);
    }
}
