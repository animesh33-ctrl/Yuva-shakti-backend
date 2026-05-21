package com.dto;

import com.enums.ApplicationStatus;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ApplicationResponseDTO {
    private UUID applicationId;
    private ApplicationStatus status;
}