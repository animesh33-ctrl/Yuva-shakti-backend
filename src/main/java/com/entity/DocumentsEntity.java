package com.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "documents")
@Data @NoArgsConstructor
public class DocumentsEntity {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne @JoinColumn(name = "application_id")
    private ApplicationEntity application;

    private String passportPhoto;
    private String identityCard;
    private String tenthMarksheet;
    private String twelfthMarksheet;
    private String previousSemesterMarksheet;
}