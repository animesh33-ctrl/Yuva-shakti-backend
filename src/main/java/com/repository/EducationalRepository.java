package com.repository;

import com.entity.ApplicationEntity;
import com.entity.EducationInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EducationalRepository extends JpaRepository<EducationInfoEntity, UUID> {
    Optional<EducationInfoEntity> findByApplication(ApplicationEntity application);
}