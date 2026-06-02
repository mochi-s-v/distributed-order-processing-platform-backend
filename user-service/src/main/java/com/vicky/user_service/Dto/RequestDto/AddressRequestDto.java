package com.vicky.user_service.Dto.RequestDto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressRequestDto {
    private int doorNo;
    private String Street;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private boolean isDefault;
}
