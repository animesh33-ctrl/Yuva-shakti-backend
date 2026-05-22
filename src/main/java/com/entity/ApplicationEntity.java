package com.entity;

import com.enums.ApplicationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity @Table(name = "applications")
@NoArgsConstructor @Builder @AllArgsConstructor @Getter @Setter
public class ApplicationEntity {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status = ApplicationStatus.DRAFT;

    @OneToOne(mappedBy = "application", cascade = CascadeType.ALL)
    private PersonalInfoEntity personalInfo;

    @OneToOne(mappedBy = "application", cascade = CascadeType.ALL)
    private EducationInfoEntity educationInfo;

    @OneToOne(mappedBy = "application", cascade = CascadeType.ALL)
    private FinancialInfoEntity financialInfo;

    @OneToOne(mappedBy = "application", cascade = CascadeType.ALL)
    private BankInfoEntity bankInfo;

    @OneToOne(mappedBy = "application", cascade = CascadeType.ALL)
    private DocumentsEntity documents;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
}
