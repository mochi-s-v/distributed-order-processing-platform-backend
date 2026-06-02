package com.vicky.user_service.Mapper;

import com.vicky.user_service.Dto.RequestDto.AddressRequestDto;
import com.vicky.user_service.Dto.ResponseDto.AddressResponseDto;
import com.vicky.user_service.Entity.AddressEntity;

import java.time.LocalDateTime;

public class AddressMapper {
    // to entity
    public static AddressEntity toEntity(AddressRequestDto addressRequestDto) {
        if (addressRequestDto == null) {
            return null;
        }

        return AddressEntity.builder()
                .doorNo(addressRequestDto.getDoorNo())
                .street(addressRequestDto.getStreet())
                .city(addressRequestDto.getCity())
                .state(addressRequestDto.getState())
                .postalCode(addressRequestDto.getPostalCode())
                .country(addressRequestDto.getCountry())
                .isDefault(addressRequestDto.isDefault())
                .createdAt(LocalDateTime.now())
                .build();
    }

    // to dto
    public static AddressResponseDto toDto(AddressEntity addressEntity) {
        if (addressEntity == null) {
            return null;
        }

        return AddressResponseDto.builder()
                .addressId(addressEntity.getId())
                .doorNo(addressEntity.getDoorNo())
                .street(addressEntity.getStreet())
                .city(addressEntity.getCity())
                .state(addressEntity.getState())
                .postalCode(addressEntity.getPostalCode())
                .country(addressEntity.getCountry())
                .isDefault(addressEntity.isDefault())
                .build();
    }
}
