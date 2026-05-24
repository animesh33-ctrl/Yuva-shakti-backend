package com.repository;

import com.entity.ApplicationEntity;
import com.entity.BankInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BankRepository extends JpaRepository<BankInfoEntity, UUID> {
    Optional<BankInfoEntity> findByApplication(ApplicationEntity application);
}