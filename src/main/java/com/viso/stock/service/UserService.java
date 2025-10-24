package com.viso.stock.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.viso.stock.model.CreateUserDto;
import com.viso.stock.model.UserEntity;
import com.viso.stock.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<UserEntity> findById(UUID id) {
        return userRepository.findById(id);
    }

    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    public UUID createUser(CreateUserDto dto) {
        // obtain PasswordEncoder from security config
        com.viso.stock.config.SecurityConfig securityConfig = new com.viso.stock.config.SecurityConfig(userRepository, null);
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        String hash = passwordEncoder.encode(dto.getPassword());
        UserEntity user = new UserEntity(dto.getFullName(), dto.getEmail(), hash, null);
        userRepository.save(user);
        return user.getId();
    }


}
