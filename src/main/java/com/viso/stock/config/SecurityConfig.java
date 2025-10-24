package com.viso.stock.config;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.viso.stock.exceptions.NotFoundException;
import com.viso.stock.model.UserEntity;
import com.viso.stock.repository.UserRepository;
import com.viso.stock.service.DbSeedService;

import jakarta.annotation.PostConstruct;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    private final UserRepository userRepository;
    private final DbSeedService seedService;

    public SecurityConfig(UserRepository userRepository, DbSeedService dbSeedService) {
        this.userRepository = userRepository;
        this.seedService = dbSeedService;
    }

    @PostConstruct
    public void init() {
        System.out.println("Initializing database seeding...");
        try {
            seedService.seedFromEnums();
        } catch (Exception e) {
            System.err.println("Error during database seeding: " + e.getMessage());
            System.out.println("Stack trace:");
            e.printStackTrace(System.out);
        }
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> {
            UserEntity user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new NotFoundException("User not found", "User"));

            Set<GrantedAuthority> auths = user.getRoles().stream().flatMap((role -> {
                Set<GrantedAuthority> s = role.getPermissions().stream()
                        .map(perm -> {
                            return new SimpleGrantedAuthority(perm.getName().getAuthority());
                        })
                        .collect(Collectors.toSet());
                return s.stream();
            })).collect(Collectors.toSet());

            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    auths);
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public/**").permitAll()
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

}