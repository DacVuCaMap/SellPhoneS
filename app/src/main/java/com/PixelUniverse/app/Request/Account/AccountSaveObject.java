package com.PixelUniverse.app.Request.Account;

import com.PixelUniverse.app.Entity.Role;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class AccountSaveObject {
    private Long id;
    private String email;
    private String name;
    private String avatar;
    private String phoneNumber;
    private Date updateAt;
    private boolean isLocked;
    //role
    // flag ? "admin" == "user"
    private Integer role;
}
