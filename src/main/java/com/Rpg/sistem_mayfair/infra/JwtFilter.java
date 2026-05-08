package com.Rpg.sistem_mayfair.infra;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        System.out.println("JWT FILTER EXECUTADO");

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        try {

            String token = authHeader.substring(7);

            System.out.println("TOKEN RECEBIDO");

            if (jwtService.isTokenValid(token)) {

                String username =
                        jwtService.extractUsername(token);

                List<String> roles =
                        jwtService.extractRoles(token);

                List<SimpleGrantedAuthority> authorities =
                        roles.stream()
                                .map(role -> {

                                    String finalRole =
                                            role.startsWith("ROLE_")
                                                    ? role
                                                    : "ROLE_" + role;

                                    return new SimpleGrantedAuthority(finalRole);
                                })
                                .toList();

                System.out.println("USERNAME: " + username);
                System.out.println("ROLES: " + roles);
                System.out.println("AUTHORITIES: " + authorities);

                User principal = new User(
                        username,
                        "",
                        authorities
                );

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                principal,
                                null,
                                authorities
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder.clearContext();

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);

                System.out.println("AUTHENTICATION SETADA");
            }

        } catch (Exception e) {

            System.out.println("ERRO JWT:");
            e.printStackTrace();
        }

        filterChain.doFilter(request, response);
    }
}