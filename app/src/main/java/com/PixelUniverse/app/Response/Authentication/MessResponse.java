package com.PixelUniverse.app.Response.Authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;


public class MessResponse {
    public void setMessage (HttpServletResponse httpServletResponse, String mess){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonResponse =objectMapper.writeValueAsString(Collections.singletonMap("message",mess));
            httpServletResponse.setContentType("application/json");
            httpServletResponse.getWriter().write(jsonResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
