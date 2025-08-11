package com.ecommerce.app.controller;

import com.ecommerce.app.dto.OrderDto;
import com.ecommerce.app.entity.Order;
import com.ecommerce.app.entity.User;
import com.ecommerce.app.mapper.OrderMapper;
import com.ecommerce.app.service.OrderService;
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
//@RequestMapping("/api/v1/orders")
//@CrossOrigin(origins = "*")
@Deprecated
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderMapper orderMapper;
    
    private Optional<User> getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userService.findByUsername(username);
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> getUserOrders() {
        return getAuthenticatedUser()
                .map(user -> ResponseEntity.ok(orderMapper.toDtoList(orderService.getUserOrders(user))))
                .orElse(ResponseEntity.status(401).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long id) {
        Optional<User> userOpt = getAuthenticatedUser();
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).build();
        }

        Optional<Order> orderOpt = orderService.getOrderById(id);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            // Security check: Ensure the user owns this order
            if (!order.getUser().getId().equals(userOpt.get().getId())) {
                return ResponseEntity.status(403).build(); // Forbidden
            }
            return ResponseEntity.ok(orderMapper.toDto(order));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody(required = false) Map<String, Object> request) {
        Optional<User> userOpt = getAuthenticatedUser();
        if (userOpt.isPresent()) {
            try {
                String discountCode = (request != null) ? (String) request.get("discountCode") : null;
                Order order = orderService.createOrder(userOpt.get(), discountCode);
                return ResponseEntity.ok(orderMapper.toDto(order));
            } catch (RuntimeException e) {
                // Return a 400 error with the actual error message
                return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
            }
        }
        return ResponseEntity.status(401).build();
    }

    @PutMapping("/{id}/process")
    public ResponseEntity<String> processOrder(@PathVariable Long id) {
        orderService.processOrder(id);
        return ResponseEntity.ok("Order processed successfully");
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<String> completeOrder(@PathVariable Long id) {
        orderService.completeOrder(id);
        return ResponseEntity.ok("Order completed successfully");
    }
}
