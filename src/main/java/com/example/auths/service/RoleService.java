package com.example.auths.service;

import com.example.auths.model.Role;
import com.example.auths.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public List<Role> getAllRoles() {
        return roleRepository.findByDeletedAtIsNull();
    }

    public Role getRoleById(UUID id) {
        return roleRepository.findByIdAndDeletedAtIsNull(id).orElse(null);
    }

    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    public Role updateRole(UUID id, Role roleData) {
        Role role = roleRepository.findByIdAndDeletedAtIsNull(id).orElseThrow();

        role.setCode(roleData.getCode());
        role.setName(roleData.getName());
        role.setDescription(roleData.getDescription());
        if (roleData.getIsSystem() != null) {
            role.setIsSystem(roleData.getIsSystem());
        }

        return roleRepository.save(role);
    }

    public void deleteRole(UUID id) {
        deleteById(id);
    }

    public List<Role> searchRoles(String name, String code) {
        String safeName = name == null ? "" : name.trim();
        String safeCode = code == null ? "" : code.trim();
        return roleRepository.findByNameContainingIgnoreCaseAndCodeContainingIgnoreCaseAndDeletedAtIsNull(safeName,
                safeCode);
    }

    public List<Role> getActiveRoles(Boolean isActive) {
        return roleRepository.findByIsActiveAndDeletedAtIsNull(isActive);
    }

    public List<Role> getSystemRoles() {
        return roleRepository.findByIsSystemTrueAndDeletedAtIsNull();
    }

    public Page<Role> getRolesPagination(int page, int size, String sortField, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return roleRepository.findByDeletedAtIsNull(pageable);
    }

    public Role lockRole(UUID id) {
        Role role = roleRepository.findByIdAndDeletedAtIsNull(id).orElseThrow();
        role.setIsActive(false);
        return roleRepository.save(role);
    }

    public Role unlockRole(UUID id) {
        Role role = roleRepository.findByIdAndDeletedAtIsNull(id).orElseThrow();
        role.setIsActive(true);
        return roleRepository.save(role);
    }

    public void deleteById(UUID id) {
        Role role = roleRepository.findByIdAndDeletedAtIsNull(id).orElseThrow();
        if (role.getDeletedAt() == null) {
            role.setDeletedAt(LocalDateTime.now());
        }
        roleRepository.save(role);
    }
}
