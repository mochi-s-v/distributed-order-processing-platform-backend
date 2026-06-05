package com.vicky.order_service.Config;


import com.vicky.order_service.Utility.GetAttributesFromHeader;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfig {

    @Value("${gateway.shared.secret}")
    private String sharedSecret;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                requestTemplate.header("X-Gateway-Secret", sharedSecret);
                try {
                    String username = GetAttributesFromHeader.getAuthUsername();
                    String role = GetAttributesFromHeader.getAuthRole();
                    requestTemplate.header("X-loggedIn-user", username);
                    requestTemplate.header("X-loggedIn-role", role);
                } catch (Exception e) {
                    requestTemplate.header("X-loggedIn-user", "SYSTEM");
                    requestTemplate.header("X-loggedIn-role", "ROLE_SYSTEM");
                }
            }
        };
    }
}
