package com.PixelUniverse.app.Service;

import com.PixelUniverse.app.Entity.Account;
import com.PixelUniverse.app.Entity.Token;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public interface TokenService {

    String generateToken(UserDetails acc, boolean remember);
    void revokedTokenByAccount(Account account) ;
    void revokedTokenByToken(Token token) ;

    boolean checkValidToken(String tokenString);

    String extraTokenToEmail(String tokenString);

    String getTokenFromRequest(HttpServletRequest request);
}
