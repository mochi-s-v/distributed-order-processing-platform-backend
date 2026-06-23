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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    @CacheEvict(value = "addresses", key = "T(com.vicky.user_service.Utility.GetAuthenticatedUser).getAuthUsername()")
    public List<AddressResponseDto> addAddress(AddressRequestDto addressRequestDto) {
        AddressEntity addressEntity = AddressMapper.toEntity(addressRequestDto);
        UserEntity userEntity = userRepository.findByUsername(GetAuthenticatedUser.getAuthUsername())
                        .orElseThrow(() ->  new RuntimeException("User not found"));
        addressEntity.setUserEntity(userEntity);
        userEntity.getAddressEntityList().add(addressEntity);
        addressRepository.save(addressEntity);
        return userEntity.getAddressEntityList().stream()
                .map(address -> AddressMapper.toDto(address))
                .collect(Collectors.toList());
    }

    @Override
    @CacheEvict(value = "addresses", key = "T(com.vicky.user_service.Utility.GetAuthenticatedUser).getAuthUsername()")
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
                    .collect(Collectors.toList());
        } else {
            throw new RuntimeException("Access denied");
        }
    }

    @Override
    @Cacheable(value = "addresses", key = "T(com.vicky.user_service.Utility.GetAuthenticatedUser).getAuthUsername()")
    public List<AddressResponseDto> getAddress() {
        UserEntity userEntity = userRepository.findByUsername(GetAuthenticatedUser.getAuthUsername())
                .orElseThrow(() ->  new RuntimeException("User not found"));
        return userEntity.getAddressEntityList().stream()
                .map(address -> AddressMapper.toDto(address))
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "addresses", key = "T(com.vicky.user_service.Utility.GetAuthenticatedUser).getAuthUsername() + ':' + #addressId")
    public AddressResponseDto getAddressById(long addressId) {
        UserEntity userEntity = userRepository.findByUsername(GetAuthenticatedUser.getAuthUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        AddressEntity addressEntity = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));
        if (userEntity.getAddressEntityList().contains(addressEntity)) {
            return AddressMapper.toDto(addressEntity);
        } else {
            throw new RuntimeException("access denied");
        }
    }

    @Override
    public boolean getAddressByIdValid(long addressId) {
        UserEntity userEntity = userRepository.findByUsername(GetAuthenticatedUser.getAuthUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        AddressEntity addressEntity = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));
        return userEntity.getAddressEntityList().contains(addressEntity);
    }
}
