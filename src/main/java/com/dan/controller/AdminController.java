package com.dan.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/area")
    @PreAuthorize("hasRole('admin')")
    public String areaRestrita() {
        return "Você é admin e tem acesso!";
    }
}
