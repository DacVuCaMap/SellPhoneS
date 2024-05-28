package com.PixelUniverse.app.Controller;


import com.PixelUniverse.app.Request.Authentication.LoginRequest;
import com.PixelUniverse.app.Request.Authentication.RegisterRequest;
import com.PixelUniverse.app.Service.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    @GetMapping("/login")
    public ResponseEntity<?> LoginAccount(@RequestBody  LoginRequest loginRequest, HttpServletResponse httpServletResponse){
        return authenticationService.LoginAccount(loginRequest,httpServletResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<?> RegisterAccount(@RequestBody RegisterRequest registerRequest){
        return authenticationService.RegisterAccount(registerRequest);
    }
}
