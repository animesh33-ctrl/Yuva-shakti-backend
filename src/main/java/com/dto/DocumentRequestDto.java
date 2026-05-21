package com.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Getter
public class DocumentRequestDto {
    private MultipartFile passportPhoto;
    private MultipartFile identityCard;
    private MultipartFile tenthMarksheet;
    private MultipartFile twelfthMarksheet;
    private MultipartFile previousSemesterMarksheet;
}
