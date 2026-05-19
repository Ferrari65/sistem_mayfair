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

                /*
                 * =========================
                 * CSRF
                 * =========================
                 */
                .csrf(csrf -> csrf.disable())

                /*
                 * =========================
                 * CORS
                 * =========================
                 */
                .cors(Customizer.withDefaults())

                /*
                 * =========================
                 * SESSIONLESS JWT
                 * =========================
                 */
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                /*
                 * =========================
                 * AUTORIZAÇÃO
                 * =========================
                 */
                .authorizeHttpRequests(auth -> auth

                        /*
                         * =========================
                         * ROTAS PÚBLICAS
                         * =========================
                         */
                        .requestMatchers(
                                "/admin/login",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        /*
                         * DEBUG
                         */
                        .requestMatchers(
                                "/personagens/debug-auth"
                        ).permitAll()

                        /*
                         * =========================
                         * GETS PÚBLICOS
                         * =========================
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
                         * =========================
                         * REAÇÕES / LIKES
                         * =========================
                         */
                        .requestMatchers(
                                HttpMethod.POST,
                                "/jornal/*/like",
                                "/jornal/*/reacao"
                        ).permitAll()

                        /*
                         * =========================
                         * UPLOAD FOTO ESTABELECIMENTO
                         * =========================
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
                        .requestMatchers(
                                HttpMethod.POST,
                                "/personagens/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/personagens/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/personagens/**"
                        ).hasRole("ADMIN")

                        /*
                         * =========================
                         * RESTANTE AUTENTICADO
                         * =========================
                         */
                        .anyRequest().authenticated()
                )

                /*
                 * =========================
                 * JWT FILTER
                 * =========================
                 */
                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        /*
         * ORIGENS
         */
        configuration.setAllowedOriginPatterns(List.of("*"));

        /*
         * MÉTODOS
         */
        configuration.setAllowedMethods(List.of(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "PATCH",
                "OPTIONS"
        ));

        /*
         * HEADERS
         */
        configuration.setAllowedHeaders(List.of("*"));

        /*
         * CREDENTIALS
         */
        configuration.setAllowCredentials(false);

        /*
         * REGISTRO
         */
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }
}