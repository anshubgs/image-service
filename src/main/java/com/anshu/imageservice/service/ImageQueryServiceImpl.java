package com.anshu.imageservice.service;

import com.anshu.imageservice.client.DeviceServiceClient;
import com.anshu.imageservice.dto.ImageResponse;
import com.anshu.imageservice.exception.ImageNotFoundException;
import com.anshu.imageservice.model.Image;
import com.anshu.imageservice.model.ImageMetadata;
import com.anshu.imageservice.repository.ImageMetadataRepository;
import com.anshu.imageservice.repository.ImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ImageQueryServiceImpl implements ImageQueryService {

    @Autowired
    private RedisTemplate<String, Object> objectRedisTemplate;

    private final ImageRepository imageRepository;
    private final ImageMetadataRepository metadataRepository;
    private final DeviceValidationService validateDevice;
    private final DeviceServiceClient serviceClient;

    public ImageQueryServiceImpl(
            ImageRepository imageRepository,
            ImageMetadataRepository metadataRepository,DeviceValidationService validateDevice,
            DeviceServiceClient serviceClient) {
        this.imageRepository = imageRepository;
        this.metadataRepository = metadataRepository;
        this.validateDevice = validateDevice;
        this.serviceClient = serviceClient;
    }

    // 🔹 View image (NO cache)
    @Override
    public ImageResponse getImage(UUID imageUuid) {

        Image img = imageRepository.findByUuid(imageUuid)
                .orElseThrow(() ->
                        new ImageNotFoundException("Image not found"));

        ImageMetadata meta =
                metadataRepository.findByImageUuid(imageUuid)
                        .orElse(null);

        return map(img, meta);
    }

    @Override
    public ImageResponse getLatestImage(UUID deviceUuid) {

        // 1️⃣ Validate device
       // validateDevice.validateDevice(deviceUuid);
        serviceClient.validateDevice(deviceUuid,null);

        String cacheKey = "latest:" + deviceUuid;

        // 2️⃣ Try to fetch from Redis first
        ImageResponse cached = (ImageResponse) objectRedisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            log.info("[CACHE HIT] latest image for device={}", deviceUuid);
            return cached;
        }

        log.info("[CACHE MISS] latest image for device={}", deviceUuid);

        // 3️⃣ Fetch from DB
        Image img = imageRepository
                .findTopByDeviceUuidOrderByCapturedAtDesc(deviceUuid)
                .orElseThrow(() ->
                        new ImageNotFoundException("No image found"));

        ImageMetadata meta = metadataRepository.findByImageUuid(img.getUuid())
                .orElse(null);

        ImageResponse response = map(img, meta);

        // 4️⃣ Save to Redis with TTL (60 seconds)
        objectRedisTemplate.opsForValue().set(cacheKey, response, Duration.ofSeconds(60));

        return response;
    }


    @Override
    public List<ImageResponse> listImages(UUID deviceUuid, Pageable pageable) {

        // 🔐 1️⃣ Validate device first
       // validateDevice.validateDevice(deviceUuid);
        serviceClient.validateDevice(deviceUuid,null);

        List<Image> images =
                imageRepository.findByDeviceUuid(deviceUuid, pageable).getContent();

        if (images.isEmpty()){
            throw new ImageNotFoundException("No Image Found For This Device");
        }

        // 1️⃣ sab image UUID nikaalo
        List<UUID> imageUuids =
                images.stream()
                        .map(Image::getUuid)
                        .toList();

        // 2️⃣ ek hi query me metadata lao
        Map<UUID, ImageMetadata> metadataMap =
                metadataRepository.findByImageUuidIn(imageUuids)
                        .stream()
                        .collect(Collectors.toMap(
                                ImageMetadata::getImageUuid,
                                m -> m
                        ));

        // 3️⃣ map karo
        return images.stream()
                .map(img -> map(img, metadataMap.get(img.getUuid())))
                .toList();
    }


    private ImageResponse map(Image img, ImageMetadata meta) {
        return ImageResponse.builder()
                .imageUuid(img.getUuid())
                .deviceUuid(img.getDeviceUuid())
                .status(img.getStatus())
                .imageUrl(meta != null ? meta.getImageUrl() : null)
                .capturedAt(img.getCapturedAt())
                .build();
    }
}
