package com.Rpg.sistem_mayfair.controller;

import com.Rpg.sistem_mayfair.dto.AdminLoginDTO;
import com.Rpg.sistem_mayfair.dto.TokenResponse;
import com.Rpg.sistem_mayfair.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody AdminLoginDTO dto) {
        return ResponseEntity.ok(adminService.login(dto));
    }

    @GetMapping("/debug-auth")
    public Object debugAuth(Authentication auth) {

        System.out.println("AUTH: " + auth);

        if (auth == null) {
            return "AUTH NULL";
        }

        return auth.getAuthorities();
    }
}
