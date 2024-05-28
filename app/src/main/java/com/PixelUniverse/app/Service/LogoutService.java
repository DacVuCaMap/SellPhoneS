package com.PixelUniverse.app.Service;

import com.PixelUniverse.app.Entity.Token;
import com.PixelUniverse.app.Repository.TokenRepository;
import com.PixelUniverse.app.Response.Authentication.MessResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LogoutService implements LogoutHandler {
    private final TokenService tokenService;
    private final TokenRepository tokenRepository;
    private final CookieService cookieService;
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String tokenString = tokenService.getTokenFromRequest(request);
        MessResponse messResponse = new MessResponse();
        if (tokenString==null){
            // send response error
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            messResponse.setMessage(response,"Logout failed! not found token");
            return;
        }
        Token token = tokenRepository.findByTokenString(tokenString).orElse(null);
        if (token==null){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            messResponse.setMessage(response,"Logout failed! not found token");
            return;
        }
        //revoked token
        tokenService.revokedTokenByToken(token);
        SecurityContextHolder.clearContext();
        Cookie cookie = cookieService.setCookieValue("jwt","/",true,0,null);
        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
        messResponse.setMessage(response,"Logout success");
    }
}
