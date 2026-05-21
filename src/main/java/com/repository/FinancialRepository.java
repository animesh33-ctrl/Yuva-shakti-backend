package com.repository;

import com.entity.FinancialInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FinancialRepository extends JpaRepository<FinancialInfoEntity, UUID> {
}