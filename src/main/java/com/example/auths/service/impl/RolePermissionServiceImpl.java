package com.example.auths.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.auths.model.RolePermission;
import com.example.auths.repository.RolePermissionRepository;
import com.example.auths.service.RolePermissionService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class RolePermissionServiceImpl implements RolePermissionService {

    private final RolePermissionRepository repository;

    public RolePermissionServiceImpl(RolePermissionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<RolePermission> getAll(Pageable pageable) {
        return repository.findByDeletedAtIsNull(pageable);
    }

    @Override
    public RolePermission getById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bản ghi gán quyền"));
    }

    @Override
    public RolePermission assignPermission(UUID roleId, UUID permissionId) {
        RolePermission rp = new RolePermission();
        rp.setId(UUID.randomUUID());
        rp.setRoleId(roleId);
        rp.setPermissionId(permissionId);
        rp.setCreatedAt(LocalDateTime.now());
        rp.setIsActive(true);
        return repository.save(rp);
    }

    @Override
    public RolePermission update(UUID id, RolePermission updateData) {
        RolePermission existing = getById(id);
        if (updateData.getPermissionId() != null) {
            existing.setPermissionId(updateData.getPermissionId());
        }
        existing.setUpdatedAt(LocalDateTime.now());
        return repository.save(existing);
    }

    @Override
    public void softDelete(UUID id) {
        RolePermission rp = getById(id);
        rp.setDeletedAt(LocalDateTime.now());
        rp.setIsActive(false);
        repository.save(rp);
    }

    @Override
    public List<RolePermission> getPermissionsByRole(UUID roleId) {
        return repository.findActivePermissionsByRoleId(roleId);
    }

    @Override
    public List<RolePermission> getRolesByPermission(UUID permissionId) {
        return repository.findActiveRolesByPermissionId(permissionId);
    }

    @Override
    public Page<RolePermission> searchByRoleId(UUID roleId, Pageable pageable) {
        return repository.findByRoleIdAndDeletedAtIsNull(roleId, pageable);
    }

    @Override
    public List<RolePermission> getByIsActive(Boolean isActive) {
        return repository.findByIsActiveAndDeletedAtIsNull(isActive);
    }

    @Override
    public RolePermission lock(UUID id) {
        RolePermission rp = getById(id);
        rp.setIsActive(false);
        rp.setUpdatedAt(LocalDateTime.now());
        return repository.save(rp);
    }

    @Override
    public RolePermission unlock(UUID id) {
        RolePermission rp = getById(id);
        rp.setIsActive(true);
        rp.setUpdatedAt(LocalDateTime.now());
        return repository.save(rp);
    }
}
