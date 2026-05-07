package com.Rpg.sistem_mayfair.service;

import com.Rpg.sistem_mayfair.domain.Admin;
import com.Rpg.sistem_mayfair.dto.AdminLoginDTO;
import com.Rpg.sistem_mayfair.dto.TokenResponse;
import com.Rpg.sistem_mayfair.infra.JwtService;
import com.Rpg.sistem_mayfair.repository.AdminRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AdminService(AdminRepository adminRepository,
                        PasswordEncoder passwordEncoder,
                        JwtService jwtService) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public TokenResponse login(AdminLoginDTO dto) {
        Admin admin = adminRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("Admin não encontrado"));

        if (!passwordEncoder.matches(dto.getPassword(), admin.getPassword())) {
            throw new RuntimeException("Senha inválida");
        }

        String token = jwtService.generateToken(
                admin.getUsername(),
                List.of("ADMIN")
        );

        return new TokenResponse(token);
    }
}