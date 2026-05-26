package com.example.auths.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.auths.model.UserRole;
import com.example.auths.service.UserRoleService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user-roles")
public class UserRoleController {

    private final UserRoleService service;

    public UserRoleController(UserRoleService service) {
        this.service = service;
    }

    @GetMapping
    public Page<UserRole> getAll(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {
        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.fromString(sort.split(",")[1]), sort.split(",")[0]));
        return service.getAll(pageable);
    }

    @GetMapping("/{id}")
    public UserRole getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @PostMapping
    public UserRole create(@RequestBody UserRole userRole) { // body chỉ cần userId, roleId
        return service.assignRole(userRole.getUserId(), userRole.getRoleId());
    }

    @PutMapping("/{id}")
    public UserRole update(@PathVariable UUID id, @RequestBody UserRole updateData) {
        return service.update(id, updateData);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/{userId}/roles")
    public List<UserRole> getRolesByUser(@PathVariable UUID userId) {
        return service.getRolesByUser(userId);
    }

    @GetMapping("/roles/{roleId}/users")
    public List<UserRole> getUsersByRole(@PathVariable UUID roleId) {
        return service.getUsersByRole(roleId);
    }

    @GetMapping("/search")
    public Page<UserRole> search(@RequestParam UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return service.searchByUserId(userId, PageRequest.of(page, size));
    }

    @GetMapping("/active")
    public List<UserRole> getByIsActive(@RequestParam Boolean isActive) {
        return service.getByIsActive(isActive);
    }

    @PutMapping("/{id}/lock")
    public UserRole lock(@PathVariable UUID id) {
        return service.lock(id);
    }

    @PutMapping("/{id}/unlock")
    public UserRole unlock(@PathVariable UUID id) {
        return service.unlock(id);
    }
}
