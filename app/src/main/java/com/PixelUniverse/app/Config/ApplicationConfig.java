package com.PixelUniverse.app.Config;

import com.PixelUniverse.app.Repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@AllArgsConstructor
public class ApplicationConfig {
    private final AccountRepository accountRepository;
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public ModelMapper modelMapper(){return new ModelMapper();}
    @Bean
    public PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
    @Bean
    public UserDetailsService userDetailsService(){
        return username -> accountRepository
                .findByEmail(username)
                .orElseThrow(()->new UsernameNotFoundException("khong tim thay"));
    }

}
