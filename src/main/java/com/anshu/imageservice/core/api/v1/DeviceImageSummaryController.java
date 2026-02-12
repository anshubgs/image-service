package com.anshu.imageservice.core.api.v1;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anshu.imageservice.core.api.v1.dto.PaginatedDeviceImageSummaryResponse;
import com.anshu.imageservice.core.application.DeviceImageSummaryApplicationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/core/devices")
@RequiredArgsConstructor
public class DeviceImageSummaryController {

    private final DeviceImageSummaryApplicationService service;

    @GetMapping("/image-summary")
    public ResponseEntity<PaginatedDeviceImageSummaryResponse> summary(
            @RequestHeader("X-USER-UUID") UUID userUuid,
            @RequestHeader("X-USER-ROLE") String role,
            Pageable pageable
    ) {

    	PaginatedDeviceImageSummaryResponse response = service.getDeviceImageSummaries(userUuid,role,pageable);
    	return ResponseEntity.ok(response);
    }
}
