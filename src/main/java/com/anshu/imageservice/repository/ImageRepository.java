package com.anshu.imageservice.repository;

import com.anshu.imageservice.dto.ImageResponse;
import com.anshu.imageservice.model.Image;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

//import java.awt.print.Pageable;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ImageRepository extends JpaRepository<Image,Long> {
    boolean existsByUuid(UUID uuid);
   Optional<Image>  findByUuid(UUID uuid);

   // List<ImageResponse> findByDeviceUuid(UUID deviceUuid, Pageable pageable);
    Page<Image> findByDeviceUuid(UUID deviceUuid, Pageable pageable);


    // <T> ScopedValue<T> findTopByDeviceUuidOrderByCapturedAtDesc(UUID deviceUuid);

//    Page<Image> findByDeviceUuid(UUID deviceUuid, Pageable pageable);
//
    Optional<Image> findTopByDeviceUuidOrderByCapturedAtDesc(UUID deviceUuid);
}
