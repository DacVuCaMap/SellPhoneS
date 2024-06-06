package com.PixelUniverse.app.Service;

import com.PixelUniverse.app.Request.Authentication.LoginRequest;
import com.PixelUniverse.app.Request.Authentication.RegisterRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface AuthenticationService {
    ResponseEntity<?> LoginAccount(LoginRequest loginRequest, HttpServletResponse httpServletResponse);
    ResponseEntity<?> RegisterAccount(RegisterRequest registerRequest,MultipartFile image);

    ResponseEntity<?> AddAccount(RegisterRequest registerRequest, MultipartFile image);
}
