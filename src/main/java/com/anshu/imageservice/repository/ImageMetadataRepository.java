package com.anshu.imageservice.repository;

import com.anshu.imageservice.model.ImageMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ImageMetadataRepository extends JpaRepository<ImageMetadata, Long> {

    Optional<ImageMetadata> findByUuid(UUID uuid);

    Optional<ImageMetadata> findByImageUuid(UUID imageUuid);

    // ✅ N+1 FIX — single query
    List<ImageMetadata> findByImageUuidIn(List<UUID> imageUuids);
}
