package com.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;


@Entity @Table(name = "personal_info")
@Getter
@Setter
@NoArgsConstructor
public class PersonalInfoEntity {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne @JoinColumn(name = "application_id")
    private ApplicationEntity application;

    private String fullName;
    private LocalDate dateOfBirth;
    private String gender;

    @Column(unique = true)
    private String aadhaarNumber;
    private String mobileNumber;
}