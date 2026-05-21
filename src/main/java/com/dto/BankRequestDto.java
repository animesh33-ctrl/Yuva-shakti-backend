package com.dto;

import lombok.Getter;
import lombok.Setter;

@Getter  @Setter
public class BankRequestDto {
    private String accountHolderName;
    private String bankName;
    private String accountNumber;
    private String ifscCode;
}
