package com.repository;

import com.entity.ApplicationEntity;
import com.entity.EducationInfoEntity;
import com.entity.FinancialInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FinancialRepository extends JpaRepository<FinancialInfoEntity, UUID> {
    Optional<FinancialInfoEntity> findByApplication(ApplicationEntity application);
}