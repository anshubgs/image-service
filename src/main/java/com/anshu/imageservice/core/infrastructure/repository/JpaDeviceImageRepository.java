package com.anshu.imageservice.core.infrastructure.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.anshu.imageservice.core.domain.repository.DeviceImageRepository;
import com.anshu.imageservice.model.Image;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class JpaDeviceImageRepository implements DeviceImageRepository {

    private final DeviceImageJpaRepository jpa;

    @Override
    public List<Image> findTop5ByDeviceUuid(UUID deviceUuid) {
        return jpa.findTop5(deviceUuid, PageRequest.of(0, 5));
    }

    @Override
    public Page<Image> findByDeviceAndDateRange(
            UUID deviceUuid,
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageable
    ) {
        return jpa.findByDeviceAndDateRange(deviceUuid, start, end, pageable);
    }
}
