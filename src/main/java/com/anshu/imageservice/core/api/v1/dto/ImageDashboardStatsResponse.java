package com.anshu.imageservice.core.api.v1.dto;

import lombok.Builder;

@Builder
public record ImageDashboardStatsResponse(
		  long totalImages
		  ) {

}
