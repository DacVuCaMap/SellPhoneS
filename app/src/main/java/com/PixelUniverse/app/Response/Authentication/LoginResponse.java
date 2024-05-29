package com.PixelUniverse.app.Response.Authentication;

import com.PixelUniverse.app.Entity.Role;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@NoArgsConstructor
@Data
public class LoginResponse {
    private String mess;
    private String token;
    private Set<Role> roles;
}
