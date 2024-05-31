package com.PixelUniverse.app.Service;

import com.PixelUniverse.app.Entity.Role;
import com.PixelUniverse.app.Repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    public Role getRoleFromFlag(boolean flag){
        String name = (flag ? "admin_role" : "user_role");
        Optional<Role> roleOptional = roleRepository.findByName(name);
        return roleOptional.orElse(null);
    }
}
