package com.anshu.imageservice.core.domain.repository;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.anshu.imageservice.core.infrastructure.entity.DeviceSummary;


public interface DeviceSummaryRepository {
	
	 long sumTotalImages();

	    List<DeviceSummary> findAllOrderByLastImageAtDesc();
	    Page<DeviceSummary> findAll(Pageable pageable);

}
