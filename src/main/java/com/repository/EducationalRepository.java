package com.repository;

import com.entity.EducationInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EducationalRepository extends JpaRepository<EducationInfoEntity, UUID> {
}