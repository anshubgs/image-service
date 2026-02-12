package com.anshu.imageservice.core.api.v1;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anshu.imageservice.core.api.v1.dto.ImageDashboardStatsResponse;
import com.anshu.imageservice.core.application.ImageDashboardStatsApplicationService;
import com.anshu.imageservice.security.UserAccessValidator;

@RestController
@RequestMapping("/api/v1/core/dashboard")
public class ImageDashboardStatsController {
	
	private final ImageDashboardStatsApplicationService summaryApplicationService;
    private final UserAccessValidator accessValidator;

	
	public ImageDashboardStatsController(ImageDashboardStatsApplicationService summaryApplicationService,UserAccessValidator accessValidator) {
		this.summaryApplicationService = summaryApplicationService;
		this.accessValidator = accessValidator;
	}
	
	@GetMapping("/image-stats")
	public ResponseEntity<ImageDashboardStatsResponse> imageStats(
			 @RequestHeader("X-USER-UUID") UUID uuid,
             @RequestHeader("X-USER-ROLE") String userRole){
		
		accessValidator.validateAdmin(uuid, userRole);
		
		ImageDashboardStatsResponse response = summaryApplicationService.getDashboardImageStats(uuid, userRole);
		return ResponseEntity.ok(response);
	}

}
