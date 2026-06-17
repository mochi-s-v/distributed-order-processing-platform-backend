package com.vicky.user_service.Controller;


import com.vicky.user_service.Dto.RequestDto.LoginRequestDto;
import com.vicky.user_service.Dto.RequestDto.UserRequestDto;
import com.vicky.user_service.Dto.ResponseDto.ApiResponse;
import com.vicky.user_service.Dto.ResponseDto.UserResponseDto;
import com.vicky.user_service.Entity.UserEntity;
import com.vicky.user_service.Repository.UserRepository;
import com.vicky.user_service.Utility.JwtUtility;
import com.vicky.user_service.serviceImpl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserServiceImpl userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtility jwtUtility;

    public UserController(UserServiceImpl userService,
                          AuthenticationManager authenticationManager,
                          JwtUtility jwtUtility) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtility = jwtUtility;
    }

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponseDto>> createUser(@RequestBody UserRequestDto userRequestDto) {
        UserResponseDto userResponseDto = userService.createUser(userRequestDto);
        return ResponseEntity.ok(ApiResponse.success(userResponseDto, "user saved successfully", 200));
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateUser(@RequestBody UserRequestDto userRequestDto) {
        UserResponseDto userResponseDto = userService.updateUser(userRequestDto);
        return ResponseEntity.ok(ApiResponse.success(userResponseDto, "user updated successfully", 200));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<Void>> deleteUser() {
        userService.deleteUser();
        return ResponseEntity.ok(ApiResponse.success(null, "User deleted successfully", 200));
    }

    @GetMapping("/get")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUser() {
        UserResponseDto userResponseDto = userService.getUser();
        return ResponseEntity.ok(ApiResponse.success(userResponseDto, "user fetched successfully", 200));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, String>>> loginUser(@RequestBody LoginRequestDto loginRequestDto) throws Exception {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                    loginRequestDto.getUsername(),
                    loginRequestDto.getPassword()
            ));
            System.out.println("im authenticated");
            String role = authentication.getAuthorities().stream()
                    .findFirst()
                    .map(authority -> authority.getAuthority()) // <-- Explicit lambda instead of method reference
                    .orElse("ROLE_USER");
            String jwtToken = jwtUtility.generateToken(loginRequestDto.getUsername(), role, userService.getEmail(loginRequestDto.getUsername()));
            String refreshToken = jwtUtility.generateRefreshToken(loginRequestDto.getUsername());
            return ResponseEntity.ok(ApiResponse
                    .success(Map.of("accessToken : ", jwtToken,
                                          "refreshToken : ", refreshToken),
                    "logged in successfully",
                    200));
        } catch (Exception e) {
            System.out.println(e.getClass().getName());
            System.out.println(e.getMessage());
            throw e;
        }
    }

//    @PostMapping("/refresh")
//    public ResponseEntity<ApiResponse<Map<String, String>>> loginUser(@RequestBody Map<String, String> map){
//        String username = map.get("refreshToken");
//        String accessToken = jwtUtility.generateToken(username);
//            return ResponseEntity.ok(ApiResponse
//                    .success(Map.of("access token : ", jwtToken,
//                                    "refresh token : ", refreshToken),
//                            "logged in successfully",
//                            200));
//    }

    @GetMapping("/debug-headers")
    public ResponseEntity<Map<String, String>> debugHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }

        return ResponseEntity.ok(headers);
    }
}
