package com.anshu.imageservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder
@Data
@Table(name = "device_cache", schema = "image")
@NoArgsConstructor
@AllArgsConstructor
public class DeviceCache {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID uuid;

    @Column(name = "device_type")
    private String deviceType;

    @Column(name = "device_name")
    private String deviceName;
    private String status;

    @Column(name = "device_secret")
    private String deviceSecret;

    @Column(name = "last_synced_at")
    private LocalDateTime lastSyncedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "user_uuid")
    private UUID userUuid; // logical relation, not FK
}
