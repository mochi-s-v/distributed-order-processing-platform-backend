package com.vicky.user_service.Seeder;

import com.vicky.user_service.Entity.UserEntity;
import com.vicky.user_service.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.existsByRole("ADMIN")){
            return;
        }
        UserEntity admin = new UserEntity();
        admin.setEmail("admin@admin.com");
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setPhoneNumber("0000000000");
        admin.setRole("ADMIN");
        admin.setCreatedAt(LocalDateTime.now());
        admin.setUpdatedAt(LocalDateTime.now());
        userRepository.save(admin);
        System.out.println("Default admin created.");
    }
}
