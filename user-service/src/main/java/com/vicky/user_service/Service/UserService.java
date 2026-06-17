package com.vicky.user_service.Service;

import com.vicky.user_service.Dto.RequestDto.UserRequestDto;
import com.vicky.user_service.Dto.ResponseDto.UserResponseDto;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {

    // users
    public UserResponseDto createUser(UserRequestDto userRequestDto);
    public UserResponseDto updateUser(UserRequestDto userRequestDto);
    public void deleteUser();
    public UserResponseDto getUser();

    // system
    public UserDetails loadUserByUsername(String username);
    public String getEmail(String username);
}
