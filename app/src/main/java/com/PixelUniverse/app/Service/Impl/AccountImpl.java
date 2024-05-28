package com.PixelUniverse.app.Service.Impl;

import com.PixelUniverse.app.Entity.Account;
import com.PixelUniverse.app.Repository.AccountRepository;
import com.PixelUniverse.app.Service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class AccountImpl implements AccountService {
    private final AccountRepository accountRepository;
    @Override
    public List<Account> getAllAccount() {
        return null;
    }

    @Override
    public Account findAccByEmail(String email) {
        Optional<Account> acc = accountRepository.findByEmail(email);
        return acc.orElse(null);
    }
}
