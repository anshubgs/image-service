package com.anshu.imageservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "image_metadata", schema = "image")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ImageMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID uuid;

    @Column(name = "image_uuid", nullable = false)
    private UUID imageUuid;
    

    @Column(name = "device_uuid")
    private UUID deviceUuid;

    @Column(name = "original_filename")
    private String originalFilename;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    private Integer width;
    private Integer height;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
