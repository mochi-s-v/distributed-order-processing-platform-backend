package com.vicky.user_service.serviceImpl;


import com.vicky.user_service.Dto.RequestDto.AddressRequestDto;
import com.vicky.user_service.Dto.ResponseDto.AddressResponseDto;
import com.vicky.user_service.Entity.AddressEntity;
import com.vicky.user_service.Entity.UserEntity;
import com.vicky.user_service.Mapper.AddressMapper;
import com.vicky.user_service.Repository.AddressRepository;
import com.vicky.user_service.Repository.UserRepository;
import com.vicky.user_service.Service.AddressService;
import com.vicky.user_service.Utility.GetAuthenticatedUser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressServiceImpl(AddressRepository addressRepository,
                              UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<AddressResponseDto> addAddress(AddressRequestDto addressRequestDto) {
        AddressEntity addressEntity = AddressMapper.toEntity(addressRequestDto);
        UserEntity userEntity = userRepository.findByUsername(GetAuthenticatedUser.getAuthUsername())
                        .orElseThrow(() ->  new RuntimeException("User not found"));
        addressEntity.setUserEntity(userEntity);
        userEntity.getAddressEntityList().add(addressEntity);
        addressRepository.save(addressEntity);
        return userEntity.getAddressEntityList().stream()
                .map(address -> AddressMapper.toDto(address))
                .toList();
    }

    @Override
    public List<AddressResponseDto> deleteAddress(long addressId) {
        AddressEntity addressEntity = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));
        UserEntity userEntity = userRepository.findByUsername(GetAuthenticatedUser.getAuthUsername())
                .orElseThrow(() ->  new RuntimeException("User not found"));
        if (userEntity.getAddressEntityList().contains(addressEntity)) {
            userEntity.getAddressEntityList().remove(addressEntity);
            addressRepository.delete(addressEntity);
            return userEntity.getAddressEntityList().stream()
                    .map(address -> AddressMapper.toDto(address))
                    .toList();
        } else {
            throw new RuntimeException("Access denied");
        }
    }

    @Override
    public List<AddressResponseDto> getAddress() {
        UserEntity userEntity = userRepository.findByUsername(GetAuthenticatedUser.getAuthUsername())
                .orElseThrow(() ->  new RuntimeException("User not found"));
        return userEntity.getAddressEntityList().stream()
                .map(address -> AddressMapper.toDto(address))
                .toList();
    }
}
