package com.vicky.order_service.Dto.ResponseDto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressResponseDto {
    private long addressId;
    private int doorNo;
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;
}