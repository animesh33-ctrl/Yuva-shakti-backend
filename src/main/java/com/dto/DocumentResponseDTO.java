package com.dto;

import com.enums.DocumentType;
import lombok.Data;

@Data
public class DocumentResponseDTO {

    private DocumentType documentType;

    private String fileUrl;

    private boolean verified;
}