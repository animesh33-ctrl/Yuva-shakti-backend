package com.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "education_info")
@Data
@NoArgsConstructor
public class EducationInfoEntity {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "application_id")
    private ApplicationEntity application;

    private String highestQualification;
    private String institutionName;
    private String passingYear;
    private String percentage;
}