package com.repository;

import com.entity.ApplicationEntity;
import com.entity.DocumentEntity;
import com.enums.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DocumentRepository extends JpaRepository<DocumentEntity, UUID> {
    List<DocumentEntity>
    findByApplication(ApplicationEntity application);

    Optional<DocumentEntity>
    findByApplicationAndDocumentType(
            ApplicationEntity application,
            DocumentType documentType);
}