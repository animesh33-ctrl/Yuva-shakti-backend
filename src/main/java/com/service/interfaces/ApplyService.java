package com.service.interfaces;

import com.dto.*;

import java.io.IOException;

public interface ApplyService {
    ApplicationResponseDTO start();
    ApplicationResponseDTO personal(PersonalRequestDto personalRequestDto);
    ApplicationResponseDTO education(EducationRequestDto requestDto);
    ApplicationResponseDTO financial(FinancialRequestDto requestDto);
    ApplicationResponseDTO bank(BankRequestDto requestDto);
    ApplicationResponseDTO document(DocumentRequestDto requestDto) throws IOException;
    ApplicationResponseDTO submit();
    ApplicationResponseDTO getMyApplication();
}
