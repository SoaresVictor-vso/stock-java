package com.viso.stock.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viso.stock.model.CreateUserDto;
import com.viso.stock.model.UserEntity;
import com.viso.stock.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('user:read')")
    public UserEntity getUserById(@PathVariable UUID id) {
        UserEntity user = userService.findById(id).orElseThrow();
        return user;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('user:read')")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        System.out.println("Fetching all users...");
        List<UserEntity> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('user:write')")
    public UUID createUser(@RequestBody CreateUserDto createUserDto) {
        UUID userId = userService.createUser(createUserDto);
        return userId;
    }
}
