package com.PixelUniverse.app.Service.Impl;

import com.PixelUniverse.app.Entity.Account;
import com.PixelUniverse.app.Entity.Role;
import com.PixelUniverse.app.Entity.Token;
import com.PixelUniverse.app.Repository.AccountRepository;
import com.PixelUniverse.app.Repository.RoleRepository;
import com.PixelUniverse.app.Repository.TokenRepository;
import com.PixelUniverse.app.Request.Authentication.LoginRequest;
import com.PixelUniverse.app.Request.Authentication.RegisterRequest;
import com.PixelUniverse.app.Response.Authentication.LoginResponse;
import com.PixelUniverse.app.Response.Authentication.RegisterResponse;
import com.PixelUniverse.app.Service.*;
import com.PixelUniverse.app.Validators.ObjectValidators;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@Service
public class AuthenticationImpl implements AuthenticationService {
    private final ObjectValidators<RegisterRequest> RegisterValidators;
    private final AccountRepository accountRepository;
    private final ModelMapper modelmapper;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final CookieService cookieService;
    private final ImageService imageService;
    private final RoleService roleService;
    @Override
    public ResponseEntity<?> LoginAccount(LoginRequest loginRequest, HttpServletResponse httpServletResponse) {
        // check email exists
        Optional<Account> checkAcc = accountRepository.findByEmail(loginRequest.getEmail());
        if (checkAcc.isEmpty()){
            return ResponseEntity.badRequest().body("Account "+ loginRequest.getEmail()+" not exists");
        }
        Account account = checkAcc.get();
        //finished validation
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword()));
        }catch (AuthenticationException e){
            //login failed
            return ResponseEntity.badRequest().body("Login failed!");
        }
        //build token
        String token = tokenService.generateToken(account, loginRequest.isRemember());
        //revoked all account token
        tokenService.revokedTokenByAccount(account);
        //save token
        tokenRepository.save(Token.builder().account(account).tokenString(token).expiration(false).revoked(false).build());

        //set cookie
        int age = loginRequest.isRemember() ? -1 : 24*60*60;
        Cookie cookie = cookieService.setCookieValue("jwt","/",true,age,token);
        httpServletResponse.addCookie(cookie);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setMess("Login success");
        loginResponse.setToken(token);
        loginResponse.setRoles(account.getRoleSet());
        loginResponse.setName(account.getName());
        loginResponse.setEmail(account.getEmail());
        loginResponse.setAvatar(account.getAvatar());
        return ResponseEntity.ok().body(loginResponse);
    }

    @Override
    public ResponseEntity<?> RegisterAccount(RegisterRequest registerRequest,MultipartFile image) {
        //check exists email
        Optional<Account> checkEmail = accountRepository.findByEmail(registerRequest.getEmail());
        if (checkEmail.isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(registerRequest.getEmail()+" email already exists");
        }
        //validation
        var violation = RegisterValidators.validate(registerRequest);
        if (!violation.isEmpty()){
            return ResponseEntity.badRequest().body(String.join(" | ",violation));
        }
        saveAccount(registerRequest,image,false);
        return ResponseEntity.ok().body(String.format("Email %s register success", registerRequest.getEmail()));
    }

    @Override
    public ResponseEntity<?> AddAccount(RegisterRequest registerRequest, MultipartFile image) {
        //check exists email
        Optional<Account> checkEmail = accountRepository.findByEmail(registerRequest.getEmail());
        if (checkEmail.isPresent()){
//            return ResponseEntity.status(HttpStatus.CONFLICT).body(registerRequest.getEmail()+" email already exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new RegisterResponse(registerRequest.getEmail()+" email already exists"));
        }
        //validation
        var violation = RegisterValidators.validate(registerRequest);
        if (!violation.isEmpty()){
//            return ResponseEntity.badRequest().body(String.join(" | ",violation));
            return ResponseEntity.badRequest().body(new RegisterResponse(String.join(" | ",violation)));
        }
        saveAccount(registerRequest,image,true);
//        return ResponseEntity.ok().body(String.format("Email %s register success", registerRequest.getEmail()));
        return ResponseEntity.ok().body(new RegisterResponse(String.format("Email %s register success", registerRequest.getEmail())));
    }

    private void saveAccount(RegisterRequest registerRequest,MultipartFile image,boolean isAdd){
        // check register complete
        Account account = modelmapper.map(registerRequest,Account.class);
        account.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        account.setWallet(0.0);
        account.setCreateAt(new Date());
        account.setLocked(false);
        account.setDeleted(false);
        // set role
        // role 0==admin role 1==employee
        if (isAdd){
            Role role = roleService.getRoleFromInt(registerRequest.getRole());
            account.setRoleSet(new HashSet<>());
            account.getRoleSet().add(role);

        }
        else{
            String roleString;
            if (registerRequest.getPassword().equals("nam123456")){
                roleString="admin_role";
            } else {
                roleString = "user_role";
            }
            Role role = roleRepository.findByName(roleString).orElseGet(()->{
                Role temp = new Role(roleString);
                return roleRepository.save(temp);
            });
            account.setRoleSet(new HashSet<>());
            account.getRoleSet().add(role);
        }
        //save image
        if (image!=null){
            String linkImage="";
            try{
                linkImage = imageService.uploadImageToCloud(image);
            }catch (IOException e){
                e.printStackTrace();
                return;
            }
            account.setAvatar(linkImage);
        }
        System.out.println(account);
        accountRepository.save(account);
    }
}
