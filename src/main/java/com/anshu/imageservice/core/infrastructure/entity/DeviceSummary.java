package com.anshu.imageservice.core.infrastructure.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // ✅ Required by JPA
@AllArgsConstructor
@Table(name = "device_summary", schema = "image")
public class DeviceSummary {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID uuid;

    @Column(name = "device_uuid", nullable = false, unique = true)
    private UUID deviceUuid;

    @Column(name = "total_images", nullable = false)
    private int totalImages;
    
    @Column(name = "device_name")
    private String deviceName;

    @Column(name = "active_images")
    private int activeImages;

    @Column(name = "last_image_at")
    private LocalDateTime lastImageAt;

    @Column(name = "latest_image_uuid")
    private UUID latestImageUuid;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


}
