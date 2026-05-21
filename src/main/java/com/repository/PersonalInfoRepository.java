package com.repository;

import com.entity.PersonalInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PersonalInfoRepository extends JpaRepository<PersonalInfoEntity, UUID> {
}
