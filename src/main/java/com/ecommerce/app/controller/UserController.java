package com.ecommerce.app.controller;

import com.ecommerce.app.dto.UserDto;
import com.ecommerce.app.entity.User;
import com.ecommerce.app.mapper.UserMapper;
import com.ecommerce.app.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final IUserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserController(IUserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserProfile() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            
            return userService.findByUsername(username)
                    .map(userMapper::toDto)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error retrieving user profile: " + e.getMessage());
        }
    }

    @PutMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateUserProfile(@RequestBody UserDto userDto) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            // Validate required fields
            if (userDto.getEmail() == null || userDto.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Email is required");
            }

            Optional<User> userOpt = userService.findByUsername(username);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                user.setEmail(userDto.getEmail());
                if (userDto.getPhoneNumber() != null) {
                    user.setPhoneNumber(userDto.getPhoneNumber());
                }
                User updatedUser = userService.saveUser(user);
                return ResponseEntity.ok(userMapper.toDto(updatedUser));
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error updating user profile: " + e.getMessage());
        }
    }

    @PostMapping(value = "/change-password", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changePassword(@RequestBody UserDto userDto) {
        try {
            if (userDto.getNewPassword() == null || userDto.getNewPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("New password is required");
            }
            if (userDto.getNewPassword().length() < 8) {
                return ResponseEntity.badRequest().body("Password must be at least 8 characters long");
            }

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            Optional<User> userOpt = userService.findByUsername(username);
            if (userOpt.isPresent()) {
                try {
                    userService.updatePassword(userOpt.get().getId(), userDto.getNewPassword());
                    return ResponseEntity.ok("Password updated successfully");
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.badRequest().body(e.getMessage());
                } catch (Exception e) {
                    return ResponseEntity.internalServerError().body("Error updating password: " + e.getMessage());
                }
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error processing password change: " + e.getMessage());
        }
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            return userService.findById(id)
                    .map(userMapper::toDto)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error retrieving user: " + e.getMessage());
        }
    }
}
