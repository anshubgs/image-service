package com.anshu.imageservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.anshu.imageservice.model.CachedUser;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CachedUserRepository extends JpaRepository<CachedUser, Long> {

    Optional<CachedUser> findByUuid(UUID uuid);

	//boolean isDeviceAssignedToUser(UUID userUuid, UUID deviceUuid);
}