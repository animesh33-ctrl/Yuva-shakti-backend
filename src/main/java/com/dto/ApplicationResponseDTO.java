package com.dto;

import com.enums.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationResponseDTO {
    private UUID applicationId;
    private ApplicationStatus status;
    private List<DocumentResponseDTO> documents;
}