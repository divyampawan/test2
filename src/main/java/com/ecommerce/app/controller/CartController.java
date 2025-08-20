package com.ecommerce.app.controller;

import com.ecommerce.app.dto.CartItemDto;
import com.ecommerce.app.entity.User;
import com.ecommerce.app.mapper.CartItemMapper;
import com.ecommerce.app.service.interfaces.ICartService;
import com.ecommerce.app.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/cart")
@CrossOrigin(origins = "*")
public class CartController {

    private final ICartService cartService;
    private final IUserService userService;
    private final CartItemMapper cartItemMapper;

    @Autowired
    public CartController(ICartService cartService, IUserService userService, CartItemMapper cartItemMapper) {
        this.cartService = cartService;
        this.userService = userService;
        this.cartItemMapper = cartItemMapper;
    }

    private Optional<User> getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }
        String username = authentication.getName();
        return userService.findByUsername(username);
    }

    @GetMapping
    public ResponseEntity<List<CartItemDto>> getCart() {
        return getAuthenticatedUser()
                .map(user -> ResponseEntity.ok(cartItemMapper.toDtoList(cartService.getCartItems(user))))
                .orElse(ResponseEntity.status(401).build());
    }

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestBody Map<String, Object> request) {
        if (!getAuthenticatedUser().isPresent()) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            Long productId = Long.valueOf(request.get("productId").toString());
            int quantity = request.containsKey("quantity") ? Integer.parseInt(request.get("quantity").toString()) : 1;
            
            if (quantity <= 0) {
                return ResponseEntity.badRequest().body("Quantity must be greater than 0");
            }
            
            cartService.addToCart(getAuthenticatedUser().get(), productId, quantity);
            return ResponseEntity.ok("Product added to cart");
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid product ID or quantity format");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error adding product to cart");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeFromCart(@PathVariable Long id) {
        if (!getAuthenticatedUser().isPresent()) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            cartService.removeFromCart(id);
            return ResponseEntity.ok("Product removed from cart");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error removing product from cart");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCartQuantity(
            @PathVariable Long id, 
            @RequestBody Map<String, Object> request) {
                
        if (!getAuthenticatedUser().isPresent()) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            int quantity = Integer.parseInt(request.get("quantity").toString());
            if (quantity <= 0) {
                return ResponseEntity.badRequest().body("Quantity must be greater than 0");
            }
            
            cartService.updateQuantity(id, quantity);
            return ResponseEntity.ok("Cart quantity updated");
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid quantity format");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error updating cart quantity");
        }
    }
}
