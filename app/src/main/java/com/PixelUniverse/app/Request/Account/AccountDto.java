package com.PixelUniverse.app.Request.Account;

import com.PixelUniverse.app.Entity.Role;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class AccountDto {
    private Long id;
    private String email;
    private String name;
    private String avatar;
    private String phoneNumber;
    private Date updateAt;
    private Date createAt;
    private boolean isLocked;
    private Set<Role> roleSet;
}
