package com.anshu.imageservice.service;

import com.anshu.imageservice.cache.CachedDevice;

public interface DeviceCacheService {
    void save(CachedDevice device);
}
