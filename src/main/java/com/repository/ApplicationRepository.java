package com.repository;

import com.entity.ApplicationEntity;
import com.entity.UserEntity;
import com.enums.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ApplicationRepository extends JpaRepository<ApplicationEntity, UUID> {
    Optional<ApplicationEntity> findByUser(UserEntity user);
    List<ApplicationEntity> findAllByUser(UserEntity user);
    Optional<ApplicationEntity> findByUserAndStatus( UserEntity user, ApplicationStatus status);
}
