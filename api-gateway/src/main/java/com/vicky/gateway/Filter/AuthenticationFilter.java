package com.vicky.gateway.Filter;

import com.vicky.gateway.Utility.JwtUtility;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;


import java.util.List;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final JwtUtility jwtUtility;
    public AuthenticationFilter(JwtUtility jwtUtility) {
        super(Config.class);
        this.jwtUtility = jwtUtility;
    }

    public static class Config {
    }

    @Override
    public GatewayFilter apply(AuthenticationFilter.Config config) {
        return (exchange, chain) -> {
            List<String> openApiEndpoints = List.of("/api/user/register",
                    "/api/user/login");
            boolean isOpenEndpoint = openApiEndpoints.stream().anyMatch(uri ->
                    exchange.getRequest()
                            .getURI().getPath()
                            .contains(uri));
            if (isOpenEndpoint) {
                return chain.filter(exchange);
            }
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                throw new RuntimeException("no token provided");
            }
            String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new RuntimeException("issue with token");
            }
            String token = authHeader.substring(7);
            try {
                if (!jwtUtility.validateToken(token)) {
                    throw new RuntimeException("invalid token");
                }
                String username = jwtUtility.extractUsername(token);
                String rolesList = jwtUtility.extractRoles(token);
                String email = jwtUtility.extractEmail(token);
                ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                        .header("X-loggedIn-User", username)
                        .header("X-loggedIn-role", rolesList)
                        .header("X-loggedIn-email", email)
                        .build();
                return chain.filter(exchange.mutate().request(mutatedRequest).build());
            } catch (Exception e){
                System.out.println("invalid access");
                throw new RuntimeException("un authorized access to application");
            }
        };
    }
}

