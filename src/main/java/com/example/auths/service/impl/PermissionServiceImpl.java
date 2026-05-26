package com.example.auths.service.impl;

import com.example.auths.model.Permission;
import com.example.auths.repository.PermissionRepository;
import com.example.auths.service.PermissionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public List<Permission> getAllPermissions() {
        return permissionRepository.findByDeletedAtIsNull();
    }

    @Override
    public Permission getPermissionById(UUID id) {
        return permissionRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new RuntimeException("Permission not found"));
    }

    @Override
    public Permission createPermission(Permission permission) {
        return permissionRepository.save(permission);
    }

    @Override
    public Permission updatePermission(UUID id, Permission permission) {

        Permission existing = getPermissionById(id);

        existing.setCode(permission.getCode());
        existing.setName(permission.getName());
        existing.setModule(permission.getModule());
        existing.setDescription(permission.getDescription());

        return permissionRepository.save(existing);
    }

    @Override
    public void deletePermission(UUID id) {

        Permission permission = getPermissionById(id);
        if (permission.getDeletedAt() == null) {
            permission.setDeletedAt(LocalDateTime.now());
        }

        permissionRepository.save(permission);
    }

    @Override
    public List<Permission> searchPermission(String name, String code) {

        List<Permission> result = permissionRepository.findByDeletedAtIsNull();

        if (name != null) {
            result = result.stream()
                    .filter(p -> p.getName().toLowerCase().contains(name.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (code != null) {
            result = result.stream()
                    .filter(p -> p.getCode().toLowerCase().contains(code.toLowerCase()))
                    .collect(Collectors.toList());
        }

        return result;
    }

    @Override
    public List<Permission> getByModule(String module) {
        return permissionRepository.findByModuleAndDeletedAtIsNull(module);
    }

    @Override
    public List<Permission> getActivePermissions(Boolean isActive) {
        return permissionRepository.findByIsActiveAndDeletedAtIsNull(isActive);
    }

    @Override
    public List<String> getModules() {

        return permissionRepository.findByDeletedAtIsNull()
                .stream()
                .map(Permission::getModule)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public Page<Permission> getPermissionsPaging(int page, int size, String sort) {

        String[] sortArr = sort.split(",");

        Sort sortObj = Sort.by(
                Sort.Direction.fromString(sortArr[1]),
                sortArr[0]);

        Pageable pageable = PageRequest.of(page, size, sortObj);

        return permissionRepository.findByDeletedAtIsNull(pageable);
    }

    @Override
    public Permission lockPermission(UUID id) {

        Permission permission = getPermissionById(id);

        permission.setIsActive(false);

        return permissionRepository.save(permission);
    }

    @Override
    public Permission unlockPermission(UUID id) {

        Permission permission = getPermissionById(id);

        permission.setIsActive(true);

        return permissionRepository.save(permission);
    }
}
