package com.PixelUniverse.app.Service;

import com.PixelUniverse.app.Entity.Account;
import com.PixelUniverse.app.Request.Account.AccountDto;
import com.PixelUniverse.app.Request.Account.AccountSaveObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AccountService {
    List<AccountDto> getAllAccount();
    Account findAccByEmail(String email);
    ResponseEntity<?> saveAccount(AccountSaveObject accountSaveObject);
}
