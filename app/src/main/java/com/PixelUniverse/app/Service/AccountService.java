package com.PixelUniverse.app.Service;

import com.PixelUniverse.app.Entity.Account;
import com.PixelUniverse.app.Request.Account.AccountDto;
import com.PixelUniverse.app.Request.Account.AccountSaveObject;
import com.PixelUniverse.app.Request.Authentication.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface AccountService {
    List<AccountDto> getAllAccount();
    boolean AddAccount(RegisterRequest registerRequest);
    Account findAccByEmail(String email);
    ResponseEntity<?> saveAccount(AccountSaveObject accountSaveObject, MultipartFile image);
}
