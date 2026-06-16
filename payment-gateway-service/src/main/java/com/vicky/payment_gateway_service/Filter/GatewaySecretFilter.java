package com.vicky.payment_gateway_service.Filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class GatewaySecretFilter extends OncePerRequestFilter {

    @Value("${gateway.shared.secret}")
    private String gatewaySecret;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().equals("/webhook/stripe")) {
            filterChain.doFilter(request, response);
            return;
        }
        String secret = request.getHeader("X-Gateway-Secret");

        if (secret == null || !secret.equals(gatewaySecret)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Direct access forbidden: Requests must go through the API Gateway.\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
