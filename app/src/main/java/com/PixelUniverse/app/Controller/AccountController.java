package com.PixelUniverse.app.Controller;

import com.PixelUniverse.app.Repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
@AllArgsConstructor
public class AccountController {
    private final AccountRepository accountRepository;
    @GetMapping("/list")
    public ResponseEntity<?> list(){
        return ResponseEntity.ok().body(accountRepository.findAll());
    }
}
