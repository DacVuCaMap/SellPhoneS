package com.PixelUniverse.app.Service.Impl;

import com.PixelUniverse.app.Entity.Account;
import com.PixelUniverse.app.Entity.Role;
import com.PixelUniverse.app.Repository.AccountRepository;
import com.PixelUniverse.app.Request.Account.AccountDto;
import com.PixelUniverse.app.Request.Account.AccountSaveObject;
import com.PixelUniverse.app.Request.Authentication.RegisterRequest;
import com.PixelUniverse.app.Response.Authentication.RegisterResponse;
import com.PixelUniverse.app.Service.AccountService;
import com.PixelUniverse.app.Service.ImageService;
import com.PixelUniverse.app.Service.RoleService;
import com.PixelUniverse.app.Validators.ObjectValidators;
import lombok.AllArgsConstructor;
import org.apache.catalina.connector.Response;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.validation.Validator;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class AccountImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final RoleService roleService;
    private final ModelMapper modelMapper;
    private final ObjectValidators<AccountSaveObject> saveObjectObjectValidators;
    private final ImageService imageService;
    @Override
    public List<AccountDto> getAllAccount() {
        List<Account> accounts = accountRepository.findAllByIsDeletedFalse();
        return accounts.stream().map(this::mapDto).collect(Collectors.toList());
    }

    @Override
    public boolean AddAccount(RegisterRequest registerRequest) {

        return false;
    }

    @Override
    public Account findAccByEmail(String email) {
        Optional<Account> acc = accountRepository.findByEmail(email);
        return acc.orElse(null);
    }

    @Override
    public ResponseEntity<?> saveAccount(AccountSaveObject accountSaveObject, MultipartFile image) {
        Optional<Account> checkAccount = accountRepository.findById(accountSaveObject.getId());

        var violation = saveObjectObjectValidators.validate(accountSaveObject);
        if (!violation.isEmpty()){
            return ResponseEntity.badRequest().body(String.join(" | ",violation));
        }
        if (checkAccount.isEmpty()){
            return ResponseEntity.badRequest().body(new RegisterResponse(accountSaveObject.getEmail()+" not exists"));
        }
        Account account = checkAccount.get();
        if (!accountSaveObject.getEmail().equals(account.getEmail())){
            return ResponseEntity.badRequest().body(new RegisterResponse("Cannot change Email"));
        }
        BeanUtils.copyProperties(accountSaveObject,account);
        System.out.println(account);
        //if null => do not set role
        if (accountSaveObject.getRole()==null){
            accountSaveObject.setRole(3);
        }
        Role role = roleService.getRoleFromInt(accountSaveObject.getRole());
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);
        account.setRoleSet(roleSet);
        //set update at
        account.setUpdateAt(new Date());
        //save image
        if (image!=null){
            String linkImage="";
            try{
                linkImage = imageService.uploadImageToCloud(image);
            }catch (IOException e){
                e.printStackTrace();
                return ResponseEntity.badRequest().body(new RegisterResponse("Image upload failed"));
            }
            account.setAvatar(linkImage);
        }
        accountRepository.save(account);
        return ResponseEntity.ok().body(new RegisterResponse(account.getEmail()+" update success"));
    }
    public AccountDto mapDto(Account account){
        return modelMapper.map(account,AccountDto.class);
    }

}
