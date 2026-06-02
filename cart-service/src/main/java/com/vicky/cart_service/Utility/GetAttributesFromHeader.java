package com.vicky.cart_service.Utility;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class GetAttributesFromHeader {

    public static String getAuthUsername() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String username = request.getHeader("X-LoggedIn-User");

            if (username != null && !username.trim().isEmpty()) {
                return username;
            }
        }
        throw new RuntimeException("Unauthorized: No authenticated user context found in request headers.");
    }

    public static String getAuthRole() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String role = request.getHeader("X-loggedIn-role");
            if (role != null && !role.trim().isEmpty()) {
                return role;
            }
        }
        throw new RuntimeException("Unauthorized: No role provided in request headers.");
    }
}