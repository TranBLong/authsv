package com.example.auths.controller;

import com.example.auths.model.User;
import com.example.auths.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // GET /api/users
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // GET /api/users/{id}
    @GetMapping("/{id}")
    public User getUserById(@PathVariable UUID id) {
        return userService.getUserById(id);
    }

    // POST /api/users
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    // PUT /api/users/{id}
    @PutMapping("/{id}")
    public User updateUser(@PathVariable UUID id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    // POST /api/users/{id}/avatar
    @PostMapping(value = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public User uploadAvatar(@PathVariable UUID id, @RequestParam("file") MultipartFile file) {
        return userService.updateAvatar(id, file);
    }

    // DELETE /api/users/{id}
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
    }

    // GET /api/users/search?name=&username=&email=
    @GetMapping("/search")
    public List<User> searchUsers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email) {
        return userService.searchUsers(name, username, email);
    }

    // GET /api/users?userType=
    // @GetMapping(params = "userType")
    // public List<User> getUsersByType(@RequestParam String userType) {
    // return userService.getUsersByType(userType);
    // }

    // GET /api/users/active?isActive=true
    @GetMapping("/active")
    public List<User> getActiveUsers(@RequestParam Boolean isActive) {
        return userService.getActiveUsers(isActive);
    }

    // GET /api/users/page?page=0&size=10&sort=full_name,asc
    @GetMapping("/page")
    public Page<User> getUsersPage(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String sort) {
        return userService.getUsersPagination(page, size, sort);
    }

    // PUT /api/users/{id}/lock
    @PutMapping("/{id}/lock")
    public User lockUser(@PathVariable UUID id) {
        return userService.lockUser(id);
    }

    // PUT /api/users/{id}/unlock
    @PutMapping("/{id}/unlock")
    public User unlockUser(@PathVariable UUID id) {
        return userService.unlockUser(id);
    }
}
