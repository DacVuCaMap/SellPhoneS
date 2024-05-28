package com.PixelUniverse.app.Request.Authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Email is not null")
    @Email(message = "Invalid Email format")
    private String email;
    @NotBlank(message = "Password is not null")
    private String password;
    @NotBlank(message = "Name is not null")
    private String name;
    private String avatar;
    @Pattern(regexp = "^[0-9]+$",message = "Invalid PhoneNumber")
    private String phoneNumber;
}
