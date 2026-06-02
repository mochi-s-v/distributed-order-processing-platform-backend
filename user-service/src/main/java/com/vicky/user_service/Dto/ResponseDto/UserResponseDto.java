package com.vicky.user_service.Dto.ResponseDto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {
    private long id;
    private String username;
    private String email;
    private String phoneNumber;
}
