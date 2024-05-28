package com.PixelUniverse.app.Config;

import com.PixelUniverse.app.Entity.Account;
import com.PixelUniverse.app.Repository.AccountRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        Account account = accountRepository.findByEmail(email).orElse(null);
        if (account==null){
            throw new BadCredentialsException("Email not exists");
        }
        if (!passwordEncoder.matches(password,account.getPassword())){
            throw new BadCredentialsException("Wrong password");
        }

        return new UsernamePasswordAuthenticationToken(account.getEmail(), account.getPassword(), null);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
