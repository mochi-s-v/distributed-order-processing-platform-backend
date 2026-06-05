package com.vicky.user_service.Service;

import com.vicky.user_service.Dto.RequestDto.AddressRequestDto;
import com.vicky.user_service.Dto.ResponseDto.AddressResponseDto;

import java.util.List;

public interface AddressService {
    public List<AddressResponseDto> addAddress(AddressRequestDto addressRequestDto);
    public List<AddressResponseDto> deleteAddress(long addressId);
    public List<AddressResponseDto> getAddress();
    public boolean getAddressByIdValid(long addressId);
    public AddressResponseDto getAddressById(long addressId);
}
