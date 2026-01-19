package com.anshu.imageservice.repository;

import com.anshu.imageservice.model.ImageMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ImageMetadataRepository extends JpaRepository<ImageMetadata, Long> {

    Optional<ImageMetadata> findByUuid(UUID uuid);
}
