package com.ecommerce.app.controller;

import com.ecommerce.app.dto.*;
import com.ecommerce.app.entity.*;
import com.ecommerce.app.mapper.*;
import com.ecommerce.app.service.interfaces.ICartService;
import com.ecommerce.app.service.interfaces.IDiscountService;
import com.ecommerce.app.service.interfaces.IOrderService;
import com.ecommerce.app.service.interfaces.IProductService;
import com.ecommerce.app.service.interfaces.IPaymentService;
import com.ecommerce.app.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class ApiController {

    // User APIs - Moved to UserController
    /*
    @GetMapping(value = "/users/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> getUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        
        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isPresent()) {
            UserDto userDto = userMapper.toDto(userOpt.get());
            return ResponseEntity.ok(userDto);
        }
        return ResponseEntity.notFound().build();
    }
    
    @PutMapping(value = "/users/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> updateUserProfile(@RequestBody UserDto userDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        
        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setEmail(userDto.getEmail());
            user.setPhoneNumber(userDto.getPhoneNumber());
            User updatedUser = userService.saveUser(user);
            UserDto updatedUserDto = userMapper.toDto(updatedUser);
            return ResponseEntity.ok(updatedUserDto);
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping(value = "/users/change-password", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> changePassword(@RequestBody UserDto userDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        
        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isPresent()) {
            try {
                userService.updatePassword(userOpt.get().getId(), userDto.getNewPassword());
                return ResponseEntity.ok("Password updated successfully");
            } catch (RuntimeException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping(value = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        Optional<User> userOpt = userService.findById(id);
        if (userOpt.isPresent()) {
            UserDto userDto = userMapper.toDto(userOpt.get());
            return ResponseEntity.ok(userDto);
        }
        return ResponseEntity.notFound().build();
    }
    */
    
    // Product APIs - Moved to ProductController
    /*
    @GetMapping("/products")
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        try {
            List<Product> products = productService.getAllProducts();
            return ResponseEntity.ok(productMapper.toDtoList(products));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        try {
            return productService.getProductById(id)
                    .map(productMapper::toDto)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/products")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        try {
            Product product = productMapper.toEntity(productDto);
            Product savedProduct = productService.saveProduct(product);
            return ResponseEntity.ok(productMapper.toDto(savedProduct));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
        try {
            return productService.getProductById(id)
                    .map(existingProduct -> {
                        productMapper.updateProductFromDto(productDto, existingProduct);
                        Product updatedProduct = productService.saveProduct(existingProduct);
                        return ResponseEntity.ok(productMapper.toDto(updatedProduct));
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        try {
            if (productService.getProductById(id).isPresent()) {
                productService.deleteProduct(id);
                return ResponseEntity.ok("Product deleted successfully");
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error deleting product");
        }
    }
    */
    
    // Cart APIs - Moved to CartController
    /*
    @GetMapping("/cart")
    public ResponseEntity<List<CartItemDto>> getCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        
        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isPresent()) {
            List<CartItemDto> cartItems = cartItemMapper.toDtoList(cartService.getCartItems(userOpt.get()));
            return ResponseEntity.ok(cartItems);
        }
        return ResponseEntity.notFound().build();
    }
    */
    
    // Cart endpoints moved to CartController
    /*
    @PostMapping("/cart/add")
    public ResponseEntity<String> addToCart(@RequestBody Map<String, Object> request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        
        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isPresent()) {
            Long productId = Long.valueOf(request.get("productId").toString());
            int quantity = request.containsKey("quantity") ? Integer.valueOf(request.get("quantity").toString()) : 1;
            cartService.addToCart(userOpt.get(), productId, quantity);
            return ResponseEntity.ok("Product added to cart");
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/cart/{id}")
    public ResponseEntity<String> removeFromCart(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        
        cartService.removeFromCart(id);
        return ResponseEntity.ok("Product removed from cart");
    }
    
    @PutMapping("/cart/{id}")
    public ResponseEntity<List<CartItemDto>> updateCartQuantity(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        
        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            int quantity = Integer.parseInt(request.get("quantity").toString());
            cartService.updateQuantity(id, quantity);
            
            // Return the updated cart
            List<CartItem> updatedCart = cartService.getCartItems(userOpt.get());
            return ResponseEntity.ok(cartItemMapper.toDtoList(updatedCart));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    */
    
    // Order APIs - Moved to OrderController
    /*
    @GetMapping("/orders")
    public ResponseEntity<List<OrderDto>> getUserOrders() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        
        List<Order> orders = orderService.getUserOrders(userOpt.get());
        List<OrderDto> orderDtos = orderMapper.toDtoList(orders);
        return ResponseEntity.ok(orderDtos);
    }
    
    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        
        Optional<Order> orderOpt = orderService.getOrderById(id);
        if (orderOpt.isPresent()) {
            // Security check: Ensure the user owns this order
            if (!orderOpt.get().getUser().getId().equals(userOpt.get().getId())) {
                return ResponseEntity.status(403).build(); // Forbidden
            }
            return ResponseEntity.ok(orderMapper.toDto(orderOpt.get()));
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping("/orders")
    public ResponseEntity<?> createOrder(@RequestBody(required = false) Map<String, Object> request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        
        try {
            String discountCode = (request != null && request.containsKey("discountCode")) 
                ? (String) request.get("discountCode") 
                : null;
                
            Order order = orderService.createOrder(userOpt.get(), discountCode);
            return ResponseEntity.ok(orderMapper.toDto(order));
        } catch (Exception e) {
            // Return a 400 error with the actual error message
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PutMapping("/orders/{id}/process")
    public ResponseEntity<String> processOrder(@PathVariable Long id) {
        orderService.processOrder(id);
        return ResponseEntity.ok("Order processed successfully");
    }
    
    @PutMapping("/orders/{id}/complete")
    public ResponseEntity<String> completeOrder(@PathVariable Long id) {
        orderService.completeOrder(id);
        return ResponseEntity.ok("Order completed successfully");
    }
    
    // Health check endpoint
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Ecommerce API is running!");
    }
    
    // Discount APIs - Moved to DiscountController
    /*
    @GetMapping("/discounts/validate")
    public ResponseEntity<Map<String, Object>> validateDiscount(@RequestParam String code, @RequestParam BigDecimal amount) {
        Map<String, Object> response = new HashMap<>();
        if (discountService.isValidDiscount(code)) {
            BigDecimal discountAmount = discountService.calculateDiscount(code, amount);
            response.put("valid", true);
            response.put("discountAmount", discountAmount);
            response.put("message", "Discount applied successfully");

            // Get discount details
            Optional<Discount> discountOpt = discountService.getDiscountByCode(code);
            if (discountOpt.isPresent()) {
                Discount discount = discountOpt.get();
                response.put("description", discount.getDescription());
                response.put("percentage", discount.getPercentage());
            }
        } else {
            response.put("valid", false);
            response.put("message", "Invalid or expired discount code");
        }
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/discounts")
    public ResponseEntity<List<DiscountDto>> getAllDiscounts() {
        List<Discount> discounts = discountService.getAllDiscounts();
        List<DiscountDto> discountDtos = discountMapper.toDtoList(discounts);
        return ResponseEntity.ok(discountDtos);
    }
    
    @PostMapping("/discounts")
    public ResponseEntity<DiscountDto> createDiscount(@RequestBody DiscountDto discountDto) {
        Discount discount = discountMapper.toEntity(discountDto);
        Discount savedDiscount = discountService.saveDiscount(discount);
        DiscountDto savedDiscountDto = discountMapper.toDto(savedDiscount);
        return ResponseEntity.ok(savedDiscountDto);
    }
    */

    // Payment APIs - Moved to PaymentController
    /*
    @PostMapping("/payments")
    public ResponseEntity<PaymentDto> createPayment(@RequestBody CreatePaymentRequestDto requestDto) {
        try {
            PaymentDto paymentDto = paymentService.createPayment(requestDto);
            return ResponseEntity.ok(paymentDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/payments/simulate-success")
    public ResponseEntity<PaymentDto> simulateSuccessfulPayment(@RequestBody Map<String, String> payload) {
        try {
            String transactionId = payload.get("transactionId");
            if (transactionId == null || transactionId.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            PaymentDto updatedPayment = paymentService.updatePaymentStatus(transactionId, PaymentStatus.SUCCESS);
            return ResponseEntity.ok(updatedPayment);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    */
}