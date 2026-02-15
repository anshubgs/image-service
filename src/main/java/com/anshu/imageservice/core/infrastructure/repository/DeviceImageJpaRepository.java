package com.anshu.imageservice.core.infrastructure.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.anshu.imageservice.model.Image;

@Repository
public interface DeviceImageJpaRepository
        extends JpaRepository<Image, Long> {

    @Query("""
        select i from Image i
        where i.deviceUuid = :deviceUuid
        order by i.capturedAt desc
    """)
    List<Image> findTop5(UUID deviceUuid, Pageable pageable);
    
    @Query("""
    	    select i from Image i
    	    where i.deviceUuid = :deviceUuid
    	    and i.capturedAt between :start and :end
    	    order by i.capturedAt desc
    	""")
    	Page<Image> findByDeviceAndDateRange(
    	        UUID deviceUuid,
    	        LocalDateTime start,
    	        LocalDateTime end,
    	        Pageable pageable
    	);

}
