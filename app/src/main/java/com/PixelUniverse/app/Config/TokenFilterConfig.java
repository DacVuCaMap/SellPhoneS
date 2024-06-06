package com.PixelUniverse.app.Config;

import com.PixelUniverse.app.Response.Authentication.MessResponse;
import com.PixelUniverse.app.Service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
@AllArgsConstructor
public class TokenFilterConfig extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().contains("api")){
            filterChain.doFilter(request,response);
            return;
        }
        MessResponse messResponse = new MessResponse();
        String tokenString = tokenService.getTokenFromRequest(request);
        if (tokenString==null || !tokenService.checkValidToken(tokenString)){
            // send response error
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            messResponse.setMessage(response,"Authentication Error Or Login session expired|revoked");
            return;
        }
        // khai bao cho spring security context
        String email = tokenService.extraTokenToEmail(tokenString);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        UsernamePasswordAuthenticationToken authToken =new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request,response);
    }

}
