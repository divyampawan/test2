package com.ecommerce.app.controller;

import com.ecommerce.app.dto.AuthRequest;
import com.ecommerce.app.dto.AuthResponse;
import com.ecommerce.app.dto.UserDto;
import com.ecommerce.app.entity.User;
import com.ecommerce.app.mapper.UserMapper;
import com.ecommerce.app.service.JwtService;
import com.ecommerce.app.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserMapper userMapper;
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtService.generateToken(userDetails);
            Long expiresIn = jwtService.getExpirationTime();
            
            return ResponseEntity.ok(new AuthResponse(token, expiresIn));
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }
    }
    
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody UserDto userDto) {
        try {
            User user = userMapper.toEntity(userDto);
            User savedUser = userService.registerUser(user);
            UserDto savedUserDto = userMapper.toDto(savedUser);
            return ResponseEntity.ok(savedUserDto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(@RequestParam String username) {
        try {
            User user = userService.findByUsername(username).orElse(null);
            if (user != null) {
                UserDto userDto = userMapper.toDto(user);
                return ResponseEntity.ok(userDto);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
} 