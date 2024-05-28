package com.PixelUniverse.app.Service;

import com.PixelUniverse.app.Request.Authentication.LoginRequest;
import com.PixelUniverse.app.Request.Authentication.RegisterRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {
    ResponseEntity<?> LoginAccount(LoginRequest loginRequest, HttpServletResponse httpServletResponse);
    ResponseEntity<?> RegisterAccount(RegisterRequest registerRequest);
}
