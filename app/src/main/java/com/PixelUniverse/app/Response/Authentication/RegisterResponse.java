package com.PixelUniverse.app.Response.Authentication;

import lombok.Data;

@Data
public class RegisterResponse {
    private String message;
    public RegisterResponse(String mess){
        this.message=mess;
    }
}
