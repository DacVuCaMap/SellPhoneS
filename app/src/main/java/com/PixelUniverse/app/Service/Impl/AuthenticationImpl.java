package com.PixelUniverse.app.Service.Impl;

import com.PixelUniverse.app.Entity.Account;
import com.PixelUniverse.app.Entity.Role;
import com.PixelUniverse.app.Entity.Token;
import com.PixelUniverse.app.Repository.AccountRepository;
import com.PixelUniverse.app.Repository.RoleRepository;
import com.PixelUniverse.app.Repository.TokenRepository;
import com.PixelUniverse.app.Request.Authentication.LoginRequest;
import com.PixelUniverse.app.Request.Authentication.RegisterRequest;
import com.PixelUniverse.app.Service.AccountService;
import com.PixelUniverse.app.Service.AuthenticationService;
import com.PixelUniverse.app.Service.CookieService;
import com.PixelUniverse.app.Service.TokenService;
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
    @Override
    public ResponseEntity<?> LoginAccount(LoginRequest loginRequest, HttpServletResponse httpServletResponse) {
        System.out.println(loginRequest);
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
        return ResponseEntity.ok().body("Login Success");
    }

    @Override
    public ResponseEntity<?> RegisterAccount(RegisterRequest registerRequest) {
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
        // check register complete
        Account account = modelmapper.map(registerRequest,Account.class);
        account.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        account.setWallet(0.0);
        account.setCreateAt(new Date());
        account.setLocked(true);
        account.setDeleted(false);
        // set role

        Role role = roleRepository.findByName("user_role").orElseGet(()->{
            Role temp = new Role("user_role");
            return roleRepository.save(temp);
        });
        account.setRoleSet(new HashSet<>());
        account.getRoleSet().add(role);
        accountRepository.save(account);
        return ResponseEntity.ok().body(String.format("Email %s register success", account.getEmail()));
    }
}
