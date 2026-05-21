package com.repository;

import com.entity.DocumentsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DocumentRepository extends JpaRepository<DocumentsEntity, UUID> {
}