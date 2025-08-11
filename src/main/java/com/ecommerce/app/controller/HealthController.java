package com.ecommerce.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Temporarily disabled - using ApiController instead
//@RestController
//@RequestMapping("/api/v1")
@Deprecated
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Ecommerce API is running!");
    }
}
