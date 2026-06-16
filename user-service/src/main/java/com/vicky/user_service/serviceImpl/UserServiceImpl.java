package com.vicky.user_service.serviceImpl;


import com.vicky.user_service.Dto.RequestDto.UserRequestDto;
import com.vicky.user_service.Dto.ResponseDto.UserResponseDto;
import com.vicky.user_service.Entity.UserEntity;
import com.vicky.user_service.Mapper.UserMapper;
import com.vicky.user_service.Repository.UserRepository;
import com.vicky.user_service.Service.UserService;
import com.vicky.user_service.Utility.GetAuthenticatedUser;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {

        UserEntity userEntity = UserMapper.toEntity(userRequestDto);
        userEntity.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        userEntity.setRole("USER");
        userEntity.setCreatedAt(LocalDateTime.now());
        userEntity.setUpdatedAt(LocalDateTime.now());
        try {
            userRepository.save(userEntity);
        } catch (DataIntegrityViolationException dive) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User details already exists");
        }
        return UserMapper.toDto(userEntity);
    }

    @Override
    public UserResponseDto updateUser(UserRequestDto userRequestDto) {
        UserEntity userEntity = userRepository.findByUsername(GetAuthenticatedUser.getAuthUsername())
                .orElseThrow(() -> new RuntimeException("User name not found"));
        userEntity.setUsername(userRequestDto.getUsername());
        userEntity.setPhoneNumber(userRequestDto.getPhoneNumber());
        userEntity.setEmail(userRequestDto.getEmail());
        userEntity.setUpdatedAt(LocalDateTime.now());
        return UserMapper.toDto(userEntity);
    }

    @Override
    public void deleteUser() {
        UserEntity userEntity = userRepository.findByUsername(GetAuthenticatedUser.getAuthUsername())
                .orElseThrow(() -> new RuntimeException("No Matching userId found"));
        userRepository.deleteById(userEntity.getId());
    }

    @Override
    public UserResponseDto getUser() {
        UserEntity userEntity = userRepository.findByUsername(GetAuthenticatedUser.getAuthUsername())
                .orElseThrow(() -> new RuntimeException("No Matching userId found"));
        return UserMapper.toDto(userEntity);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("username not found"));
    }

}
