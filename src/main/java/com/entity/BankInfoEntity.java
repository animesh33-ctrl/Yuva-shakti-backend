package com.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "bank_info")
@Data
@NoArgsConstructor
public class BankInfoEntity {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne @JoinColumn(name = "application_id")
    private ApplicationEntity application;

    private String accountHolderName;
    private String bankName;
    private String accountNumber;
    private String ifscCode;
}
