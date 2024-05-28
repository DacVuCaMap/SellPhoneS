package com.PixelUniverse.app.Service.Impl;

import com.PixelUniverse.app.Entity.Account;
import com.PixelUniverse.app.Entity.Token;
import com.PixelUniverse.app.Repository.TokenRepository;
import com.PixelUniverse.app.Service.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TokenImpl implements TokenService {
    private final String secretKey ="1234567890ABCDEFGHIKLMNOJKLASJDHSAKJHDKASKAJSASDASDVXCASDAASDASDASD123123123";
    private byte[] getSecretKeyBytes(){ return secretKey.getBytes();}

    private final TokenRepository tokenRepository;
    public TokenImpl(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }
    @Override
    public String generateToken(UserDetails acc, boolean remember) {

        Date timeExpiration = !remember ? new Date(System.currentTimeMillis()+24*60*60*1000)
                :new GregorianCalendar(2100, Calendar.JANUARY, 1).getTime();
        return Jwts
                .builder()
                .setSubject(acc.getUsername())
                .signWith(SignatureAlgorithm.HS256,getSecretKeyBytes())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(timeExpiration)
                .compact();
    }

    @Override
    public void revokedTokenByAccount(Account account) {
        List<Token> tokenList = tokenRepository.findTokenByAccountId(account.getId());
//        tokenRepository.deleteAll(tokenList);
        tokenList.forEach(token -> {
            token.setExpiration(true);
            token.setRevoked(true);
        });

        tokenRepository.saveAll(tokenList);
    }

    @Override
    public void revokedTokenByToken(Token token) {
        token.setRevoked(true);
        token.setExpiration(true);
        tokenRepository.save(token);
    }

    @Override
    public boolean checkValidToken(String tokenString) {
        Token token = tokenRepository.findByTokenString(tokenString).orElse(null);
        return token != null && !token.isExpiration() && !token.isRevoked();
    }

    @Override
    public String extraTokenToEmail(String tokenString) {
        Claims claims = Jwts.parser().setSigningKey(getSecretKeyBytes()).parseClaimsJws(tokenString).getBody();
        return claims.getSubject();
    }

    @Override
    public String getTokenFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies!=null){
            return Arrays.stream(cookies)
                    .filter(cookie -> "jwt".equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

}
