package com.viso.stock.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viso.stock.model.LoginDto;
import com.viso.stock.model.LoginResponseModel;
import com.viso.stock.service.AuthService;

@RestController
@RequestMapping("/auth/login")
public class AuthController {
    final private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public LoginResponseModel login(@RequestBody LoginDto dto) {
        return authService.login(dto);
    }

}
