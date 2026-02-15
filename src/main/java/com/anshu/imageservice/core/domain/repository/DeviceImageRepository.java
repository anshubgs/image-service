package com.anshu.imageservice.core.domain.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.anshu.imageservice.model.Image;

public interface DeviceImageRepository {

    List<Image> findTop5ByDeviceUuid(UUID deviceUuid);
    
    Page<Image> findByDeviceAndDateRange(
            UUID deviceUuid,
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageable
    );

}
