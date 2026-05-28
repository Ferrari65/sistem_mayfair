package com.Rpg.sistem_mayfair.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfigurations {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http

                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(auth -> auth

                        /*
                         * =========================
                         * PÚBLICO
                         * =========================
                         */
                        .requestMatchers(
                                "/admin/login",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        .requestMatchers(
                                "/personagens/debug-auth"
                        ).permitAll()

                        /*
                         * GETS PÚBLICOS
                         */
                        .requestMatchers(
                                HttpMethod.GET,
                                "/personagens/**",
                                "/familias/**",
                                "/destaques/**",
                                "/historico/**",
                                "/players/**",
                                "/estabelecimentos/**",
                                "/jornal/**"
                        ).permitAll()

                        /*
                         * REAÇÕES / LIKES
                         */
                        .requestMatchers(
                                HttpMethod.POST,
                                "/jornal/*/like",
                                "/jornal/*/reacao"
                        ).permitAll()

                        /*
                         * FOTO ESTABELECIMENTO
                         */
                        .requestMatchers(
                                HttpMethod.POST,
                                "/estabelecimentos/*/fotos"
                        ).permitAll()

                        /*
                         * =========================
                         * ADMIN ONLY
                         * =========================
                         */
                        .requestMatchers(HttpMethod.POST, "/personagens/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/personagens/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/personagens/**")
                        .hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.POST,
                                "/estabelecimentos/*/movimentacoes"
                        ).hasRole("ADMIN")

                        /*
                         * =========================
                         * RESTANTE AUTENTICADO
                         * =========================
                         */
                        .requestMatchers(HttpMethod.POST, "/eventos/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/eventos/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/eventos/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/eventos/**")
                        .permitAll()
                        .anyRequest().authenticated()
                )

                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
        ));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}