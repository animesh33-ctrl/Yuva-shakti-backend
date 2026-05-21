package com.controller;

import com.advice.ApiResponse;
import com.dto.LoginRequestDTO;
import com.dto.SignUpRequestDto;
import com.security.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpRequestDto signUpRequestDto){
        return new ResponseEntity<>(authService.signUp(signUpRequestDto),  HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO,
                                   HttpServletResponse httpServletResponse){
        return new ResponseEntity<>(authService.login(loginRequestDTO, httpServletResponse), HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<?>> refreshToken(HttpServletRequest request, HttpServletResponse httpServletResponse){
        return new ResponseEntity<>(new ApiResponse<>(authService.refreshToken(request,httpServletResponse)), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<?>> logout(HttpServletRequest request, HttpServletResponse httpServletResponse){
        authService.logout(request,httpServletResponse);
        return new ResponseEntity<>(new ApiResponse<>("Logged out successfully"), HttpStatus.OK);
    }
}
