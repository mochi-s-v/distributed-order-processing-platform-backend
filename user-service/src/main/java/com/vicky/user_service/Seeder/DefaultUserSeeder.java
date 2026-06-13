package com.vicky.user_service.Seeder;

import com.vicky.user_service.Entity.AddressEntity;
import com.vicky.user_service.Entity.UserEntity;
import com.vicky.user_service.Repository.AddressRepository;
import com.vicky.user_service.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DefaultUserSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.existsByEmail("jane@jane.com")) {
            return;
        }

        UserEntity jane = new UserEntity();
        jane.setEmail("jane@jane.com");
        jane.setUsername("jane");
        jane.setPassword(passwordEncoder.encode("jane123"));
        jane.setPhoneNumber("9999999999");
        jane.setRole("USER");
        jane.setCreatedAt(LocalDateTime.now());
        jane.setUpdatedAt(LocalDateTime.now());
        userRepository.save(jane);

        AddressEntity address = new AddressEntity();
        address.setDoorNo(10);
        address.setStreet("Baker Street");
        address.setCity("Chennai");
        address.setState("Tamil Nadu");
        address.setPostalCode("600001");
        address.setCountry("India");
        address.setDefault(true);
        address.setCreatedAt(LocalDateTime.now());
        address.setUserEntity(jane);
        addressRepository.save(address);

        System.out.println("Default user jane created.");
    }
}