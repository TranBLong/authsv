package com.example.auths.service;

import com.example.auths.model.Permission;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface PermissionService {

    List<Permission> getAllPermissions();

    Permission getPermissionById(UUID id);

    Permission createPermission(Permission permission);

    Permission updatePermission(UUID id, Permission permission);

    void deletePermission(UUID id);

    List<Permission> searchPermission(String name, String code);

    List<Permission> getByModule(String module);

    List<Permission> getActivePermissions(Boolean isActive);

    List<String> getModules();

    Page<Permission> getPermissionsPaging(int page, int size, String sort);

    Permission lockPermission(UUID id);

    Permission unlockPermission(UUID id);

}