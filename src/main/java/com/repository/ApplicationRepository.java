package com.repository;

import com.entity.ApplicationEntity;
import com.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ApplicationRepository extends JpaRepository<ApplicationEntity, UUID> {
    Optional<ApplicationEntity> findByUser(UserEntity user);
}
