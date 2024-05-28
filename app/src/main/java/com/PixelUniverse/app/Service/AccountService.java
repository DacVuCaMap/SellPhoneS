package com.PixelUniverse.app.Service;

import com.PixelUniverse.app.Entity.Account;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AccountService {
    List<Account> getAllAccount();
    Account findAccByEmail(String email);
}
