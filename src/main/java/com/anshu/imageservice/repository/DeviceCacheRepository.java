package com.anshu.imageservice.repository;

import com.anshu.imageservice.model.DeviceCache;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DeviceCacheRepository extends JpaRepository<DeviceCache,Long> {


    Optional<DeviceCache> findByUuid(UUID deviceUuid);
   // boolean existsByUser_UuidAndUuid(UUID userUuid, UUID deviceUuid);
}
