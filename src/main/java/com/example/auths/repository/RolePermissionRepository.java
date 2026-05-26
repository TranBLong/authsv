package com.example.auths.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.auths.model.RolePermission;

import java.util.List;
import java.util.UUID;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, UUID> {
    List<RolePermission> findByIsActiveTrue();

    Page<RolePermission> findByIsActive(Boolean isActive, Pageable pageable);

    List<RolePermission> findByIsActiveAndDeletedAtIsNull(Boolean isActive);

    Page<RolePermission> findByIsActiveAndDeletedAtIsNull(Boolean isActive, Pageable pageable);

    List<RolePermission> findByDeletedAtIsNull();

    Page<RolePermission> findByDeletedAtIsNull(Pageable pageable);

    List<RolePermission> findByRoleId(UUID roleId);

    List<RolePermission> findByPermissionId(UUID permissionId);

    List<RolePermission> findByRoleIdAndDeletedAtIsNull(UUID roleId);

    List<RolePermission> findByPermissionIdAndDeletedAtIsNull(UUID permissionId);

    Page<RolePermission> findByRoleIdAndDeletedAtIsNull(UUID roleId, Pageable pageable);

    @Query("SELECT rp FROM RolePermission rp WHERE rp.roleId = :roleId AND rp.deletedAt IS NULL")
    List<RolePermission> findActivePermissionsByRoleId(UUID roleId);

    @Query("SELECT rp FROM RolePermission rp WHERE rp.permissionId = :permissionId AND rp.deletedAt IS NULL")
    List<RolePermission> findActiveRolesByPermissionId(UUID permissionId);
}
