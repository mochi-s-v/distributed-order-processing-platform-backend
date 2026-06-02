package com.vicky.user_service.Utility;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class GetAuthenticatedUser {
    public static String getAuthUsername() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            // 2. Pull the header injected by the API Gateway
            String username = request.getHeader("X-LoggedIn-User");

            if (username != null && !username.trim().isEmpty()) {
                return username;
            }
        }

        throw new RuntimeException("Unauthorized: No authenticated user context found in request headers.");
    }
}
