package com.ecommerce.app.controller;

import com.ecommerce.app.dto.OrderDto;
import com.ecommerce.app.entity.Order;
import com.ecommerce.app.entity.User;
import com.ecommerce.app.mapper.OrderMapper;
import com.ecommerce.app.service.interfaces.IOrderService;
import com.ecommerce.app.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private final IOrderService orderService;
    private final IUserService userService;
    private final OrderMapper orderMapper;
    
    @Autowired
    public OrderController(IOrderService orderService, IUserService userService, OrderMapper orderMapper) {
        this.orderService = orderService;
        this.userService = userService;
        this.orderMapper = orderMapper;
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
    public ResponseEntity<List<OrderDto>> getUserOrders() {
        Optional<User> userOpt = getAuthenticatedUser();
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            List<Order> orders = orderService.getUserOrders(userOpt.get());
            return ResponseEntity.ok(orderMapper.toDtoList(orders));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long id) {
        Optional<User> userOpt = getAuthenticatedUser();
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).build();
        }

        try {
            Optional<Order> orderOpt = orderService.getOrderById(id);
            if (orderOpt.isPresent()) {
                Order order = orderOpt.get();
                if (!order.getUser().getId().equals(userOpt.get().getId())) {
                    return ResponseEntity.status(403).build(); // Forbidden
                }
                return ResponseEntity.ok(orderMapper.toDto(order));
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody(required = false) Map<String, Object> request) {
        Optional<User> userOpt = getAuthenticatedUser();
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).build();
        }

        try {
            String discountCode = (request != null && request.containsKey("discountCode")) 
                ? (String) request.get("discountCode") 
                : null;
                
            Order order = orderService.createOrder(userOpt.get(), discountCode);
            return ResponseEntity.ok(orderMapper.toDto(order));
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}/process")
    public ResponseEntity<String> processOrder(@PathVariable Long id) {
        try {
            orderService.processOrder(id);
            return ResponseEntity.ok("Order processed successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error processing order");
        }
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<String> completeOrder(@PathVariable Long id) {
        try {
            orderService.completeOrder(id);
            return ResponseEntity.ok("Order completed successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error completing order");
        }
    }
}
