package com.repository;

import com.entity.ApplicationEntity;
import com.entity.FinancialInfoEntity;
import com.entity.PersonalInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PersonalInfoRepository extends JpaRepository<PersonalInfoEntity, UUID> {
    Optional<PersonalInfoEntity> findByApplication(ApplicationEntity application);
}
