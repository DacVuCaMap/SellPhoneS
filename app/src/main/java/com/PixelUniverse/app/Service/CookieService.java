package com.PixelUniverse.app.Service;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;

@Service
public class CookieService {
    public Cookie setCookieValue(String name,String path,boolean httpOnly,int age,String value){
        Cookie cookie = new Cookie(name,value);
        cookie.setDomain("localhost");
        cookie.setPath(path);
        cookie.setHttpOnly(httpOnly);
        cookie.setMaxAge(age);
        return cookie;
    }
}
