package com.dto;

import lombok.Getter;
import lombok.Setter;

@Setter  @Getter
public class EducationRequestDto {
    private String highestQualification;
    private String institutionName;
    private String passingYear;
    private String percentage;
}
