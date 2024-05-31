package com.PixelUniverse.app.Controller;

import com.PixelUniverse.app.Entity.Account;
import com.PixelUniverse.app.Repository.AccountRepository;
import com.PixelUniverse.app.Request.Account.AccountSaveObject;
import com.PixelUniverse.app.Service.AccountService;
import lombok.AllArgsConstructor;
import org.apache.catalina.connector.Response;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/account")
@AllArgsConstructor
public class AccountController {
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;
    private final AccountService accountService;
    @GetMapping("/list")
    public ResponseEntity<?> list(){
        return ResponseEntity.ok().body(accountService.getAllAccount());
    }
    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody AccountSaveObject accountSaveObject){
        return accountService.saveAccount(accountSaveObject);
    }
    @GetMapping("/delete/{id}")
    public ResponseEntity<?> delAccount(@PathVariable Long id){
        Optional<Account> account = accountRepository.findById(id);
        if (account.isEmpty()){
            return ResponseEntity.badRequest().body("Not Exists");
        }
        Account acc = account.get();
        acc.setDeleted(true);
        accountRepository.save(acc);
        return ResponseEntity.ok().body(account.get().getEmail()+" is deleted");
    }

}
