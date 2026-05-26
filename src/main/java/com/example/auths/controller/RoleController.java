package com.example.auths.controller;

import com.example.auths.model.Role;
import com.example.auths.service.RoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "*")
public class RoleController {

    @Autowired
    private RoleService roleService;

    // GET /api/roles
    @GetMapping
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    // GET /api/roles/{id}
    @GetMapping("/{id}")
    public Role getRoleById(@PathVariable UUID id) {
        return roleService.getRoleById(id);
    }

    // POST /api/roles
    @PostMapping
    public Role createRole(@Valid @RequestBody Role role) {
        return roleService.createRole(role);
    }

    // PUT /api/roles/{id}
    @PutMapping("/{id}")
    public Role updateRole(@PathVariable UUID id, @RequestBody Role role) {
        return roleService.updateRole(id, role);
    }

    // DELETE /api/roles/{id}
    @DeleteMapping("/{id}")
    public void deleteRole(@PathVariable UUID id) {
        roleService.deleteById(id);
    }

    // GET /api/roles/search?name=&code=
    @GetMapping("/search")
    public List<Role> searchRoles(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String code) {
        return roleService.searchRoles(name, code);
    }

    // GET /api/roles?isActive=true
    @GetMapping("/active")
    public List<Role> getActiveRoles(@RequestParam Boolean isActive) {
        return roleService.getActiveRoles(isActive);
    }

    // GET /api/roles/system
    @GetMapping("/system")
    public List<Role> getSystemRoles() {
        return roleService.getSystemRoles();
    }

    // GET /api/roles?page=0&size=10&sort=role_name,asc
    @GetMapping("/page")
    public Page<Role> getRolesPage(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String sortField,
            @RequestParam String sortDir) {

        return roleService.getRolesPagination(page, size, sortField, sortDir);
    }

    // PUT /api/roles/{id}/lock
    @PutMapping("/{id}/lock")
    public Role lockRole(@PathVariable UUID id) {
        return roleService.lockRole(id);
    }

    // PUT /api/roles/{id}/unlock
    @PutMapping("/{id}/unlock")
    public Role unlockRole(@PathVariable UUID id) {
        return roleService.unlockRole(id);
    }

}