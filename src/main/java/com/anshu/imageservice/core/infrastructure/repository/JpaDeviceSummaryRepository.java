package com.anshu.imageservice.core.infrastructure.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.anshu.imageservice.core.domain.repository.DeviceSummaryRepository;
import com.anshu.imageservice.core.infrastructure.entity.DeviceSummary;

@Repository
public class JpaDeviceSummaryRepository  implements DeviceSummaryRepository {
	
	private final DeviceSummaryJpaRepository jpaRepository;
	
	public  JpaDeviceSummaryRepository( DeviceSummaryJpaRepository jpaRepository) {
		this.jpaRepository = jpaRepository;
	}

	   @Override
	    public long sumTotalImages() {
	        return jpaRepository.sumTotalImages();
	    }

	    @Override
	    public List<DeviceSummary> findAllOrderByLastImageAtDesc() {
	        return jpaRepository.findAllByOrderByLastImageAtDesc();
	    }
	    
	    @Override
	    public Page<DeviceSummary> findAll(Pageable pageable) {
	        return jpaRepository.findAllByOrderByLastImageAtDesc(pageable);
	    }


}
