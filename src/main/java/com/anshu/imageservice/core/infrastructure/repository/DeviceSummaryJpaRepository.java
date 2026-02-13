package com.anshu.imageservice.core.infrastructure.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.anshu.imageservice.core.infrastructure.entity.DeviceSummary;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeviceSummaryJpaRepository
        extends JpaRepository<DeviceSummary, Long> {

    Optional<DeviceSummary> findByDeviceUuid(UUID deviceUuid);

 
    @Modifying
    @Query(value = """
        INSERT INTO image.device_summary (
            uuid,
            device_uuid,
            device_name,
            total_images,
            active_images,
            last_image_at,
            latest_image_uuid,
            latest_image_url,
            updated_at
        )
        VALUES (
            gen_random_uuid(),
            :deviceUuid,
            :deviceName,
            1,
            1,
            :capturedAt,
            :imageUuid,
            :imageUrl,
            CURRENT_TIMESTAMP
        )
        ON CONFLICT (device_uuid)
        DO UPDATE SET
            total_images = image.device_summary.total_images + 1,
            active_images = image.device_summary.active_images + 1,
            last_image_at = EXCLUDED.last_image_at,
            latest_image_uuid = EXCLUDED.latest_image_uuid,
            latest_image_url = EXCLUDED.latest_image_url,   -- ✅ NEW
            device_name = EXCLUDED.device_name,
            updated_at = CURRENT_TIMESTAMP
        """,
        nativeQuery = true)
    void upsert(
            UUID deviceUuid,
            UUID imageUuid,
            LocalDateTime capturedAt,
            String deviceName,
            String imageUrl   
    );



    
    @Query("select coalesce(sum(ds.totalImages),0) from DeviceSummary ds")
    long sumTotalImages();

    List<DeviceSummary> findAllByOrderByLastImageAtDesc();
    
    Page<DeviceSummary> findAllByOrderByLastImageAtDesc(Pageable pageable);

}
