package com.example.auths.controller;

import com.example.auths.model.Permission;
import com.example.auths.service.PermissionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    // 1. Lấy toàn bộ quyền
    @GetMapping
    public List<Permission> getAllPermissions() {
        return permissionService.getAllPermissions();
    }

    // 2. Lấy chi tiết quyền
    @GetMapping("/{id}")
    public Permission getPermissionById(@PathVariable UUID id) {
        return permissionService.getPermissionById(id);
    }

    // 3. Thêm quyền
    @PostMapping
    public Permission createPermission(@RequestBody Permission permission) {
        return permissionService.createPermission(permission);
    }

    // 4. Cập nhật quyền
    @PutMapping("/{id}")
    public Permission updatePermission(
            @PathVariable UUID id,
            @RequestBody Permission permission) {
        return permissionService.updatePermission(id, permission);
    }

    // 5. Xóa mềm
    @DeleteMapping("/{id}")
    public String deletePermission(@PathVariable UUID id) {

        permissionService.deletePermission(id);

        return "Permission deleted";
    }

    // 6. Search
    @GetMapping("/search")
    public List<Permission> searchPermission(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String code) {
        return permissionService.searchPermission(name, code);
    }

    // 7. Lấy theo module
    @GetMapping("/module/{module}")
    public List<Permission> getByModule(@PathVariable String module) {
        return permissionService.getByModule(module);
    }

    // 8. Lọc theo trạng thái
    @GetMapping("/active")
    public List<Permission> getActivePermissions(
            @RequestParam Boolean isActive) {
        return permissionService.getActivePermissions(isActive);
    }

    // 9. Lấy danh sách module
    @GetMapping("/modules")
    public List<String> getModules() {
        return permissionService.getModules();
    }

    // 10. Phân trang
    @GetMapping("/paging")
    public Page<Permission> getPermissionsPaging(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String sort) {
        return permissionService.getPermissionsPaging(page, size, sort);
    }

    // 11. Lock
    @PutMapping("/{id}/lock")
    public Permission lockPermission(@PathVariable UUID id) {
        return permissionService.lockPermission(id);
    }

    // 12. Unlock
    @PutMapping("/{id}/unlock")
    public Permission unlockPermission(@PathVariable UUID id) {
        return permissionService.unlockPermission(id);
    }

}
