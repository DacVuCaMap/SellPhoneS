package com.PixelUniverse.app.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Account")
public class Account implements UserDetails {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "name")
    private String name;
    @Column(name = "avatar")
    private String avatar;
    @Column(name = "phoneNumber")
    private String phoneNumber;
    @Column(name = "wallet")
    private Double wallet;
    @Column(name = "updateAt")
    private Date updateAt;
    @Column(name = "createAt")
    private Date createAt;
    @Column(name = "isLocked")
    private boolean isLocked;
    @Column(name = "isDeleted")
    private boolean isDeleted;
    @Column(name = "ip")
    private boolean ip;
    //role
    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name = "acc_role",joinColumns = @JoinColumn(name = "accId"),inverseJoinColumns = @JoinColumn(name = "roleId"))
    private Set<Role> roleSet;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return roleSet.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
