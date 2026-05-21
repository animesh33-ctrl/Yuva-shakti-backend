package com.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "financial_info")
@Data @NoArgsConstructor
public class FinancialInfoEntity {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne @JoinColumn(name = "application_id")
    private ApplicationEntity application;

    private String annualIncome;
    private String occupation;
    private String employmentStatus;
    private String supportNeededFor;
}