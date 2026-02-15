package com.anshu.imageservice.core.api.v1;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anshu.imageservice.core.api.v1.dto.DeviceImageResponse;
import com.anshu.imageservice.core.application.DeviceImageQueryApplicationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/core/devices")
@RequiredArgsConstructor
public class DeviceImageQueryController {

    private final DeviceImageQueryApplicationService service;

    @GetMapping("/{deviceUuid}/images/latest")
    public ResponseEntity<List<DeviceImageResponse>> getLatestImages(
            @RequestHeader("X-USER-UUID") UUID userUuid,
            @RequestHeader("X-USER-ROLE") String role,
            @PathVariable UUID deviceUuid
    ) {

        List<DeviceImageResponse> response =
                service.getLatestImages(deviceUuid,userUuid,role);
        

        return ResponseEntity.ok(response);
    }
    
    
    @GetMapping("/{deviceUuid}/images")
    public ResponseEntity<Page<DeviceImageResponse>> getImagesByDate(
            @RequestHeader("X-USER-UUID") UUID userUuid,
            @RequestHeader("X-USER-ROLE") String role,
            @PathVariable UUID deviceUuid,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @PageableDefault(size = 10) Pageable pageable
    ) {
    	Page<DeviceImageResponse> response = service.getImagesByDate(deviceUuid, date, pageable, userUuid, role);

        return ResponseEntity.ok(response);
    }

}
