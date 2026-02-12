package com.anshu.imageservice.core.application;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anshu.imageservice.client.DeviceServiceClient;
import com.anshu.imageservice.core.api.v1.dto.ImageDashboardStatsResponse;
import com.anshu.imageservice.core.domain.repository.DeviceSummaryRepository;
import com.anshu.imageservice.core.infrastructure.entity.DeviceSummary;
import com.anshu.imageservice.core.infrastructure.repository.DeviceSummaryJpaRepository;
import com.anshu.imageservice.repository.CachedUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageDashboardStatsApplicationService {
	
	  private final DeviceSummaryRepository repository;
	  private final CachedUserRepository cachedUserRepository;
	 

	  public ImageDashboardStatsResponse getDashboardImageStats(UUID uuid,
	             String userRole) {

		 
		    // 📊 Step 2: fetch stats
		    long total = repository.sumTotalImages();

	        return ImageDashboardStatsResponse.builder()
	                .totalImages(total)
	                .build();
	    }

	

	
}
