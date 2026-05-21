package com.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.validation.constraints.*;

@Data
@AllArgsConstructor
public class SignUpRequestDto {
    @NotNull(message = "Full name cannot be null")
    @NotBlank(message = "Full name cannot be blank")
    private String fullname;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\S+$).{12,20}$",
            message = "Password must be 12-20 characters long, contain at least one digit, one lowercase letter, one uppercase letter, one special character, and have no whitespace"
    )
    @NotNull(message = "Password cannot be null")
    private String password;
}
