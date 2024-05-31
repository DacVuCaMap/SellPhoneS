package com.PixelUniverse.app.Service.Impl;

import com.PixelUniverse.app.Entity.Account;
import com.PixelUniverse.app.Entity.Role;
import com.PixelUniverse.app.Repository.AccountRepository;
import com.PixelUniverse.app.Request.Account.AccountDto;
import com.PixelUniverse.app.Request.Account.AccountSaveObject;
import com.PixelUniverse.app.Service.AccountService;
import com.PixelUniverse.app.Service.RoleService;
import lombok.AllArgsConstructor;
import org.apache.catalina.connector.Response;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class AccountImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final RoleService roleService;
    private final ModelMapper modelMapper;
    @Override
    public List<AccountDto> getAllAccount() {
        List<Account> accounts = accountRepository.findAllByIsDeletedFalse();
        return accounts.stream().map(this::mapDto).collect(Collectors.toList());
    }

    @Override
    public Account findAccByEmail(String email) {
        Optional<Account> acc = accountRepository.findByEmail(email);
        return acc.orElse(null);
    }

    @Override
    public ResponseEntity<?> saveAccount(AccountSaveObject accountSaveObject) {
        Optional<Account> checkAccount = accountRepository.findById(accountSaveObject.getId());
        if (checkAccount.isEmpty()){
            return ResponseEntity.badRequest().body(accountSaveObject.getEmail()+" not exists");
        }
        Account account = checkAccount.get();
        if (!accountSaveObject.getEmail().equals(account.getEmail())){
            return ResponseEntity.badRequest().body("Cannot change Email");
        }
        BeanUtils.copyProperties(accountSaveObject,account);
        //if null => do not set role
        Role role = roleService.getRoleFromFlag(accountSaveObject.isRole());
        if (role!=null){
            Set<Role> roleSet = new HashSet<>();
            roleSet.add(role);
            account.setRoleSet(roleSet);
        }
        //set update at
        account.setUpdateAt(new Date());
        accountRepository.save(account);
        return ResponseEntity.ok().body(account.getEmail()+" update success");
    }
    public AccountDto mapDto(Account account){
        return modelMapper.map(account,AccountDto.class);
    }

}
