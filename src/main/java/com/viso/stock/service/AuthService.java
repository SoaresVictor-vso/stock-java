package com.viso.stock.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.viso.stock.exceptions.BadCredentialsException;
import com.viso.stock.model.LoginDto;
import com.viso.stock.model.LoginResponseModel;
import com.viso.stock.model.RoleEntity;
import com.viso.stock.model.TokenUserEntity;
import com.viso.stock.model.UserEntity;
import com.viso.stock.repository.UserRepository;

@Service
public class AuthService {
    final private UserRepository userRepository;
    final private PasswordEncoder passwordEncoder;
    final private JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponseModel login(LoginDto loginDto) {
        String email = loginDto.getEmail();
        String password = loginDto.getPassword();
        UserEntity user = userRepository.findByEmail(email).orElseThrow(
                () -> new BadCredentialsException("Invalid Credentials"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid Credentials");
        }

        System.out.println("Login attempt with email: " + email + " and password: " + password);

        String token = jwtService.generateToken(
                user.getId(),
                user.getName(),
                user.getRoles().stream().map(role -> role.getName()).toList());

        System.out.println("Generated token: " + token);
        System.out.println("User ID in token: " + jwtService.extractPayload(token));

        TokenUserEntity reqUser = jwtService.extractTokenUser(token);

        System.out.println("Extracted User ID: " + reqUser.getId());
        System.out.println("Extracted User Name: " + reqUser.getName());
        System.out.println("Extracted User Roles: " + reqUser.getRoles().stream().map(RoleEntity::getName).toList());
        // Perform authentication logic here
        return new LoginResponseModel(token, "refresh-token");
    }

}