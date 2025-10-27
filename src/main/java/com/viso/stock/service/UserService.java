package com.viso.stock.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.viso.stock.model.CreateUserDto;
import com.viso.stock.model.RoleEntity;
import com.viso.stock.model.UserEntity;
import com.viso.stock.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
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
        String hash = passwordEncoder.encode(dto.getPassword());
        String[] roleNames = dto.getRoleName() != null ? dto.getRoleName() : new String[]{"Default"};
        Set<RoleEntity> roles = roleService.findByNames(roleNames);

        UserEntity user = new UserEntity(dto.getFullName(), dto.getEmail(), hash, roles);
        userRepository.save(user);
        return user.getId();
    }


}
