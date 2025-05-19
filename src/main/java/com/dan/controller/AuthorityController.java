package com.dan.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthorityController {

    @GetMapping("/authorities")
    public ResponseEntity<?> getAuthorities(HttpServletRequest request) {
        List<String> roles = (List<String>) request.getAttribute("roles");

        if (roles == null) {
            return ResponseEntity.status(401).body("Token inválido ou usuário não autenticado.");
        }

        return ResponseEntity.ok(Map.of("roles", roles));
    }
}
