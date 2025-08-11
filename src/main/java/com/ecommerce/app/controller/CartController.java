package com.ecommerce.app.controller;

import com.ecommerce.app.dto.CartItemDto;
import com.ecommerce.app.entity.User;
import com.ecommerce.app.mapper.CartItemMapper;
import com.ecommerce.app.service.CartService;
import com.ecommerce.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

// Temporarily disabled - using ApiController instead
//@RestController
//@RequestMapping("/api/v1/cart")
//@CrossOrigin(origins = "*")
@Deprecated
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @Autowired
    private CartItemMapper cartItemMapper;

    private Optional<User> getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
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
        Optional<User> userOpt = getAuthenticatedUser();
        if (userOpt.isPresent()) {
            Long productId = Long.valueOf(request.get("productId").toString());
            int quantity = request.containsKey("quantity") ? Integer.parseInt(request.get("quantity").toString()) : 1;
            cartService.addToCart(userOpt.get(), productId, quantity);
            return ResponseEntity.ok("Product added to cart");
        }
        return ResponseEntity.status(401).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeFromCart(@PathVariable Long id) {
        cartService.removeFromCart(id);
        return ResponseEntity.ok("Product removed from cart");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCartQuantity(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        int quantity = Integer.parseInt(request.get("quantity").toString());
        cartService.updateQuantity(id, quantity);
        return ResponseEntity.ok("Cart quantity updated");
    }
}
