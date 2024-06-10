package com.PixelUniverse.app.Request.Account;

import com.PixelUniverse.app.Entity.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class AccountSaveObject {
    @NotNull(message = "id not null")
    private Long id;
    @NotBlank(message = "email not blank")
    private String email;
    @NotBlank(message = "name not blank")
    private String name;
    private String avatar;
    private String phoneNumber;
    private Date updateAt;
    private boolean isLocked;
    //role
    // flag ? "admin" == "user"
    private Integer role;
}
