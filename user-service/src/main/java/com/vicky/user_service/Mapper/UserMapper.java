package com.vicky.user_service.Mapper;


import com.vicky.user_service.Dto.RequestDto.UserRequestDto;
import com.vicky.user_service.Dto.ResponseDto.UserResponseDto;
import com.vicky.user_service.Entity.UserEntity;

public class UserMapper {
    public static UserEntity toEntity(UserRequestDto userRequestDto) {

        if (userRequestDto == null) {
            return null;
        }

        return UserEntity.builder()
                .username(userRequestDto.getUsername())
                .email(userRequestDto.getEmail())
                .password(userRequestDto.getPassword())
                .phoneNumber(userRequestDto.getPhoneNumber())
                .build();
    }

    public static UserResponseDto toDto(UserEntity userEntity) {

        if (userEntity == null) {
            return null;
        }

        return UserResponseDto.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .phoneNumber(userEntity.getPhoneNumber())
                .build();
    }
}
