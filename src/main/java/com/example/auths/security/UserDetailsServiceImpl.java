package com.example.auths.security;

import com.example.auths.model.User;
import com.example.auths.model.UserRole;
import com.example.auths.model.Role;
import com.example.auths.model.Permission;
import com.example.auths.repository.UserRepository;
import com.example.auths.repository.UserRoleRepository;
import com.example.auths.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameAndDeletedAtIsNull(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        List<GrantedAuthority> authorities = new ArrayList<>();

        // Load active roles for user
        List<UserRole> userRoles = userRoleRepository.findActiveRolesByUserId(user.getId());
        for (UserRole userRole : userRoles) {
            if (userRole.getIsActive() != null && !userRole.getIsActive()) {
                continue;
            }
            roleRepository.findByIdAndDeletedAtIsNull(userRole.getRoleId()).ifPresent(role -> {
                if (role.getIsActive() != null && role.getIsActive()) {
                    // Add role authority (e.g. ROLE_ADMIN)
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getCode()));

                    // Add permission authorities
                    if (role.getPermissions() != null) {
                        for (Permission permission : role.getPermissions()) {
                            if (permission.getIsActive() != null && permission.getIsActive() && permission.getDeletedAt() == null) {
                                authorities.add(new SimpleGrantedAuthority(permission.getCode()));
                            }
                        }
                    }
                }
            });
        }

        user.setAuthorities(authorities);
        return user;
    }
}
