package com.PixelUniverse.app.Controller;


import com.PixelUniverse.app.Request.Authentication.LoginRequest;
import com.PixelUniverse.app.Request.Authentication.RegisterRequest;
import com.PixelUniverse.app.Service.AuthenticationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    @PostMapping("/login")
    public ResponseEntity<?> LoginAccount(@RequestBody  LoginRequest loginRequest, HttpServletResponse httpServletResponse){
        return authenticationService.LoginAccount(loginRequest,httpServletResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<?> RegisterAccount(@RequestParam("image") MultipartFile image, @RequestParam("formJson") String formJson){
        ObjectMapper objectMapper = new ObjectMapper();
        RegisterRequest registerRequest;
        try {
            registerRequest = objectMapper.readValue(formJson, RegisterRequest.class);
        } catch (JsonProcessingException e) {
            // Xử lý ngoại lệ khi chuyển đổi JSON thất bại
            return ResponseEntity.badRequest().body("Invalid JSON data");
        }
        return authenticationService.RegisterAccount(registerRequest,image);
    }

}
