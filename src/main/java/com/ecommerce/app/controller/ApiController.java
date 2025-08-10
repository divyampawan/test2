package com.ecommerce.app.controller;

import com.ecommerce.app.dto.*;
import com.ecommerce.app.entity.*;
import com.ecommerce.app.mapper.*;
import com.ecommerce.app.service.*;
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
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private CartService cartService;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private ProductMapper productMapper;
    
    @Autowired
    private OrderMapper orderMapper;
    
    @Autowired
    private CartItemMapper cartItemMapper;
    
    @Autowired
    private DiscountMapper discountMapper;
    
    @Autowired
    private DiscountService discountService;
    
    // User APIs
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
    
    // Product APIs
    @GetMapping("/products")
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductDto> productDtos = productMapper.toDtoList(products);
        return ResponseEntity.ok(productDtos);
    }
    
    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        Optional<Product> productOpt = productService.getProductById(id);
        if (productOpt.isPresent()) {
            ProductDto productDto = productMapper.toDto(productOpt.get());
            return ResponseEntity.ok(productDto);
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping("/products")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        Product product = productMapper.toEntity(productDto);
        Product savedProduct = productService.saveProduct(product);
        ProductDto savedProductDto = productMapper.toDto(savedProduct);
        return ResponseEntity.ok(savedProductDto);
    }
    
    @PutMapping("/products/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
        Optional<Product> productOpt = productService.getProductById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setPrice(productDto.getPrice());
            product.setStockQuantity(productDto.getStockQuantity());
            Product updatedProduct = productService.saveProduct(product);
            ProductDto updatedProductDto = productMapper.toDto(updatedProduct);
            return ResponseEntity.ok(updatedProductDto);
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/products/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        Optional<Product> productOpt = productService.getProductById(id);
        if (productOpt.isPresent()) {
            productService.deleteProduct(id);
            return ResponseEntity.ok("Product deleted successfully");
        }
        return ResponseEntity.notFound().build();
    }
    
    // Cart APIs
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
    public ResponseEntity<String> updateCartQuantity(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        
        int quantity = Integer.valueOf(request.get("quantity").toString());
        cartService.updateQuantity(id, quantity);
        return ResponseEntity.ok("Cart quantity updated");
    }
    
    // Order APIs
    @GetMapping("/orders")
    public ResponseEntity<List<OrderDto>> getUserOrders() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        
        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isPresent()) {
            List<Order> orders = orderService.getUserOrders(userOpt.get());
            List<OrderDto> orderDtos = orderMapper.toDtoList(orders);
            return ResponseEntity.ok(orderDtos);
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        
        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isPresent()) {
            Optional<Order> orderOpt = orderService.getOrderById(id);
            if (orderOpt.isPresent()) {
                Order order = orderOpt.get();
                if (!order.getUser().getId().equals(userOpt.get().getId())) {
                    return ResponseEntity.status(403).build();
                }
                OrderDto orderDto = orderMapper.toDto(order);
                return ResponseEntity.ok(orderDto);
            }
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping("/orders")
    public ResponseEntity<OrderDto> createOrder(@RequestBody(required = false) Map<String, Object> request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        
        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isPresent()) {
            try {
                String discountCode = null;
                if (request != null && request.containsKey("discountCode")) {
                    discountCode = (String) request.get("discountCode");
                }
                Order order = orderService.createOrder(userOpt.get(), discountCode);
                OrderDto orderDto = orderMapper.toDto(order);
                return ResponseEntity.ok(orderDto);
            } catch (RuntimeException e) {
                return ResponseEntity.badRequest().build();
            }
        }
        return ResponseEntity.notFound().build();
    }
    
    @PutMapping("/orders/{id}/process")
    public ResponseEntity<String> processOrder(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        
        orderService.processOrder(id);
        return ResponseEntity.ok("Order processed successfully");
    }
    
    @PutMapping("/orders/{id}/complete")
    public ResponseEntity<String> completeOrder(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        
        orderService.completeOrder(id);
        return ResponseEntity.ok("Order completed successfully");
    }
    
    // Health check endpoint
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Ecommerce API is running!");
    }
    
    // Discount APIs
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
}