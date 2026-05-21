package com.controller;


import com.advice.ApiResponse;
import com.dto.*;
import com.service.interfaces.ApplyService;
import io.lettuce.core.search.arguments.AggregateArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/apply")
@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
public class ApplyController {

    private final ApplyService applyService;

    @PostMapping("/start")
    public ResponseEntity<?> start() {
        return ResponseEntity.ok(new ApiResponse<>(applyService.start()));
    }

    @PutMapping("/personal")
    public ResponseEntity<?> personal(@RequestBody  PersonalRequestDto requestDto) {
        return new ResponseEntity<>(new ApiResponse<>(applyService.personal(requestDto)), HttpStatus.OK);
    }

    @PutMapping("/education")
    public ResponseEntity<?> education(@RequestBody EducationRequestDto requestDto) {
        return new ResponseEntity<>(new ApiResponse<>(applyService.education(requestDto)), HttpStatus.OK);
    }
    @PutMapping("/financial")
    public ResponseEntity<?> financial(@RequestBody FinancialRequestDto requestDto) {
        return new ResponseEntity<>(new ApiResponse<>(applyService.financial(requestDto)), HttpStatus.OK);
    }
    @PutMapping("/bank")
    public ResponseEntity<?> bank(@RequestBody BankRequestDto requestDto) {
        return new ResponseEntity<>(new ApiResponse<>(applyService.bank(requestDto)), HttpStatus.OK);
    }
    @PostMapping("/documents")
    public ResponseEntity<?> document(@RequestParam MultipartFile passportPhoto,
                                      @RequestParam MultipartFile identityCard,
                                      @RequestParam MultipartFile tenthMarksheet,
                                      @RequestParam MultipartFile twelfthMarksheet,
                                      @RequestParam MultipartFile previousSemesterMarksheet) throws IOException {
        DocumentRequestDto requestDto = DocumentRequestDto.builder().
                passportPhoto(passportPhoto).
                identityCard(identityCard).
                tenthMarksheet(tenthMarksheet).
                twelfthMarksheet(twelfthMarksheet).
                previousSemesterMarksheet(previousSemesterMarksheet).
                build();
        return new ResponseEntity<>(new ApiResponse<>(applyService.document(requestDto)), HttpStatus.OK);
    }

    @PostMapping("/submit")
    public ResponseEntity<?> submit() {
        return new ResponseEntity<>(new ApiResponse<>(applyService.submit()), HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMyApplication() {
        return new ResponseEntity<>(new ApiResponse<>(applyService.getMyApplication()), HttpStatus.OK);
    }

}

/*

POST   /api/v1/application/start              → create empty application
PUT    /api/v1/application/personal           → save personal info
PUT    /api/v1/application/education          → save education info
PUT    /api/v1/application/financial          → save financial info
PUT    /api/v1/application/bank               → save bank info
POST   /api/v1/application/documents          → upload files (multipart)
POST   /api/v1/application/submit             → set status = SUBMITTED
GET    /api/v1/application/me                 → get current user's application
 */

//{
//        "apiError": null,
//        "data": {
    //        "fullname": "Animesh Palui",
    //        "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwYWx1aWFuaW1lc2gzMUBnbWFpbC5jb20iLCJyb2xlcyI6WyJSRUFEIiwiUk9MRV9VU0VSIiwiV1JJVEUiXSwiaWF0IjoxNzc5MzQ4MTg4LCJleHAiOjE3NzkzNDkwODh9.ERX4_JrnBniMvo0l7_T40Wwx3YIqpdmBYr54AN-bvCU"
//        },
//        "timestamp": "2026-05-21T12:53:09.0384966"
//}