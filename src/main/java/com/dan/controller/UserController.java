package com.dan.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        String username = (String) request.getAttribute("username");
        List<String> roles = (List<String>) request.getAttribute("roles");

        if (userId == null) {
            return ResponseEntity.status(401).body("Não autenticado.");
        }

        return ResponseEntity.ok(Map.of(
                "userId", userId,
                "username", username,
                "roles", roles
        ));
    }
}
