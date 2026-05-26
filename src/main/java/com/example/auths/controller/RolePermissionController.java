package com.example.auths.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.auths.model.RolePermission;
import com.example.auths.service.RolePermissionService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/role-permissions")
public class RolePermissionController {

    private final RolePermissionService service;

    public RolePermissionController(RolePermissionService service) {
        this.service = service;
    }

    @GetMapping
    public Page<RolePermission> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {
        String[] sortParts = sort.split(",");
        Sort.Direction direction = Sort.Direction.fromString(sortParts[1]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortParts[0]));
        return service.getAll(pageable);
    }

    @GetMapping("/{id}")
    public RolePermission getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @PostMapping
    public RolePermission create(@RequestBody RolePermission dto) {
        // DTO chỉ cần roleId và permissionId
        return service.assignPermission(dto.getRoleId(), dto.getPermissionId());
    }

    @PutMapping("/{id}")
    public RolePermission update(@PathVariable UUID id, @RequestBody RolePermission updateData) {
        return service.update(id, updateData);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/roles/{roleId}/permissions")
    public List<RolePermission> getPermissionsByRole(@PathVariable UUID roleId) {
        return service.getPermissionsByRole(roleId);
    }

    @GetMapping("/permissions/{permissionId}/roles")
    public List<RolePermission> getRolesByPermission(@PathVariable UUID permissionId) {
        return service.getRolesByPermission(permissionId);
    }

    @GetMapping("/search")
    public Page<RolePermission> search(
            @RequestParam UUID roleId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return service.searchByRoleId(roleId, PageRequest.of(page, size));
    }

    @GetMapping("/active")
    public List<RolePermission> getByIsActive(@RequestParam Boolean isActive) {
        return service.getByIsActive(isActive);
    }

    @PutMapping("/{id}/lock")
    public RolePermission lock(@PathVariable UUID id) {
        return service.lock(id);
    }

    @PutMapping("/{id}/unlock")
    public RolePermission unlock(@PathVariable UUID id) {
        return service.unlock(id);
    }
}
