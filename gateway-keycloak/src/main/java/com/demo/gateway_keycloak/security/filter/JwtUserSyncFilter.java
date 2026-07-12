package com.demo.gateway_keycloak.security.filter;

import com.demo.gateway_keycloak.service.UserSyncService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author HP
 **/

@Component
public class JwtUserSyncFilter extends OncePerRequestFilter {

    private final UserSyncService userSyncService;

    public JwtUserSyncFilter(UserSyncService userSyncService) {
        this.userSyncService = userSyncService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            userSyncService.syncUserFromJwt(jwt);
        }

        filterChain.doFilter(request, response);
    }
}
