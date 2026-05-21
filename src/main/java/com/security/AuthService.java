package com.security;

import com.config.JwtConfig;
import com.dto.LoginRequestDTO;
import com.dto.LoginResponseDTO;
import com.dto.SignUpRequestDto;
import com.dto.SignUpResponseDto;
import com.entity.RefreshTokenEntity;
import com.entity.UserEntity;
import com.enums.Role;
import com.exception.ResourceConflictException;
import com.repository.UserRepository;
import com.security.jwt.JwtService;
import com.security.lockoutservice.AccountLockoutService;
import com.security.tokenservice.RefreshTokenService;
import com.security.tokenservice.TokenBlacklistService;
import com.service.interfaces.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final AccountLockoutService  accountLockoutService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;
    private final JwtService jwtService;
    private final JwtConfig jwtConfig;
    private final TokenBlacklistService tokenBlacklistService;

    public SignUpResponseDto signUp(SignUpRequestDto signUpRequestDto) {
        Optional<UserEntity> userEntity = userRepository.findByFullname(signUpRequestDto.getFullname());
        if(userEntity.isPresent()) {
            throw new ResourceConflictException("User with this fullname "+signUpRequestDto.getFullname()
                    +" already exists");
        }
        UserEntity newUser = modelMapper.map(signUpRequestDto, UserEntity.class);
        newUser.setPassword(passwordEncoder.encode(signUpRequestDto.getPassword()));
        newUser.setRole(Role.USER);
        newUser = userRepository.saveAndFlush(newUser);
        return modelMapper.map(newUser, SignUpResponseDto.class);
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO, HttpServletResponse httpServletResponse) {
        try{
            userRepository.findByEmail(loginRequestDTO.getEmail())
                    .ifPresent(user -> {
                        if(!user.isAccountNonLocked()){
                            throw new AuthenticationServiceException("Account is locked until "+user.getLockedUntil());
                        }
                    });

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword())
            );

            UserDetails userDetails = userRepository.findByEmail(loginRequestDTO.getEmail()).get();

            UserEntity userEntity = userRepository.findByEmail(loginRequestDTO.getEmail()).get();

            accountLockoutService.handleSuccessfulLogin(userDetails.getUsername());

            refreshTokenService.deleteTokensByUsername(userDetails.getUsername());

            String accessToken = jwtService.generateToken(userDetails);
            String refreshToken = refreshTokenService.createRefreshToken(userEntity.getFullname()).getToken();

            Cookie cookie = new Cookie("refreshToken",refreshToken);
            cookie.setSecure(true);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge((int) (jwtConfig.getRefreshTokenExpiration()/1000)); //7 days

            httpServletResponse.addCookie(cookie);

            return new LoginResponseDTO(userEntity.getFullname() ,accessToken);
        }
        catch(AuthenticationServiceException exception){
            throw exception;
        }
        catch(Exception exception){
            try {
                userRepository.findByEmail(
                        loginRequestDTO.getEmail()
                ).ifPresent(user ->
                        accountLockoutService.handleFailedAttempt(user.getUsername())
                );
            } catch (Exception ignored) {}

            throw new AuthenticationServiceException("Invalid username or password");
        }
    }

    public LoginResponseDTO refreshToken(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        Cookie[] cookies = request.getCookies();
        if(cookies == null){
            throw new AuthenticationServiceException("No cookies found in the request and Refresh token not found inside the Cookies");
        }

        String token = Arrays.stream(cookies)
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new AuthenticationServiceException("Refresh token not found inside the Cookies"));

        RefreshTokenEntity old = refreshTokenService.verifyToken(token);
        UserDetails userDetails = userService.loadUserByUsername(old.getUsername());

        UserEntity userEntity = userRepository.findByEmail(userDetails.getUsername()).get();

        refreshTokenService.revokeToken(token);
        refreshTokenService.deleteTokensByUsername(userDetails.getUsername());
        RefreshTokenEntity newToken = refreshTokenService.createRefreshToken(userDetails.getUsername());

        Cookie cookie = new Cookie("refreshToken", newToken.getToken());
        cookie.setMaxAge((int) (jwtConfig.getRefreshTokenExpiration()/1000)); // 7 days
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);

        String newAccessToken = jwtService.generateToken(userDetails);
        return new LoginResponseDTO(userEntity.getFullname() ,newAccessToken);
    }

    public void logout(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()){
            throw new AuthenticationServiceException("Not authenticated");
        }

        String username = authentication.getName();
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String accessToken = authHeader.substring(7);
            long remainingExpiry = jwtService.getRemainingExpiry(accessToken);
            if (remainingExpiry > 0) {
                tokenBlacklistService.blacklist(accessToken, remainingExpiry);
            }
        }

        refreshTokenService.deleteTokensByUsername(username);

        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);

    }
}
