package com.demo.gateway_keycloak.security;

import com.demo.gateway_keycloak.security.filter.JwtUserSyncFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * @author HP
 **/
@Configuration
public class SecurityConfig {

    private final  String[] freeRequestUrls = {
            "/swagger-ui.html","/swagger-ui/**","/v3/api-doc/**",
            "swagger-ressources/**","/api-docs/**","/aggregate/**",
            "/v3/api-docs/swagger-config", "/v3/api-docs/**"
    };

    private final JwtUserSyncFilter jwtUserSyncFilter;

    public SecurityConfig(JwtUserSyncFilter jwtUserSyncFilter) {
        this.jwtUserSyncFilter = jwtUserSyncFilter;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                auth -> auth
                        .requestMatchers(freeRequestUrls)
                        .permitAll()
                        .anyRequest()
                        .authenticated()
        ).oauth2ResourceServer( oauth2->oauth2.jwt(Customizer.withDefaults()))
                .addFilterAfter(jwtUserSyncFilter, BearerTokenAuthenticationFilter.class)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**",config); // 🔄 applique à tous les endpoints
        return source;
    }
}
