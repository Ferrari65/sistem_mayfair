package com.Rpg.sistem_mayfair.infra;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    // CHAVE FIXA (mínimo 32 caracteres)
    private static final String SECRET =
            "minha-chave-super-secreta-com-no-minimo-32-caracteres";

    private final SecretKey key = Keys.hmacShaKeyFor(
            SECRET.getBytes(StandardCharsets.UTF_8)
    );

    // ===================== GERAR TOKEN =====================
    public String generateToken(String username, List<String> roles) {

        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() + 86400000) // 1 dia
                )
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // ===================== EXTRAIR USERNAME =====================
    public String extractUsername(String token) {

        return extractAllClaims(token)
                .getSubject();
    }

    // ===================== EXTRAIR ROLES =====================
    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {

        return extractAllClaims(token)
                .get("roles", List.class);
    }

    // ===================== VALIDAR TOKEN =====================
    public boolean isTokenValid(String token) {

        try {

            extractAllClaims(token);

            return true;

        } catch (Exception e) {

            System.out.println("ERRO JWT: " + e.getMessage());

            return false;
        }
    }

    // ===================== EXTRAIR CLAIMS =====================
    private Claims extractAllClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}