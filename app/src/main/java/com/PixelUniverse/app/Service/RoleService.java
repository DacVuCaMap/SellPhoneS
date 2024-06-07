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
    public Role getRoleFromInt(int number){
        if (number==0){
            return roleRepository.findByName("admin_role").orElseGet(()->{
                Role temp = new Role("admin_role");
                return roleRepository.save(temp);
            });
        }
        else if (number==1) {
            return roleRepository.findByName("employee_role").orElseGet(()->{
                Role temp = new Role("employee_role");
                return roleRepository.save(temp);
            });
        } else {
            return roleRepository.findByName("user_role").orElseGet(()->{
                Role temp = new Role("user_role");
                return roleRepository.save(temp);
            });
        }
    }
}
