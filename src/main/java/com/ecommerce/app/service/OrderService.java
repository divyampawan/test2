package com.ecommerce.app.service;

import com.ecommerce.app.entity.*;
import com.ecommerce.app.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private CartService cartService;
    
    @Autowired
    private DiscountService discountService;
    
    @Transactional
    public Order createOrder(User user, String discountCode) {
        List<CartItem> cartItems = cartService.getCartItems(user);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }
        
        Order order = new Order(user);
        List<OrderItem> orderItems = new ArrayList<>();
        
        BigDecimal subtotal = BigDecimal.ZERO;
        
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem(order, cartItem.getProduct(), cartItem.getQuantity());
            orderItems.add(orderItem);
            subtotal = subtotal.add(orderItem.getTotalPrice());
        }
        
        order.setOrderItems(orderItems);
        order.setSubtotal(subtotal);
        
        // Apply discount if code is provided
        BigDecimal discountAmount = BigDecimal.ZERO;
        if (discountCode != null && !discountCode.trim().isEmpty()) {
            Optional<Discount> discountOpt = discountService.getDiscountByCode(discountCode);
            if (discountOpt.isPresent()) {
                Discount discount = discountOpt.get();
                discountAmount = discount.calculateDiscount(subtotal);
                order.setDiscount(discount);
                order.setDiscountCode(discountCode);
            }
        }
        
        order.setDiscountAmount(discountAmount);
        order.setTotalAmount(subtotal.subtract(discountAmount));
        
        // Save the order first
        Order savedOrder = orderRepository.save(order);
        
        // Increment discount usage if discount was applied
        if (discountAmount.compareTo(BigDecimal.ZERO) > 0 && order.getDiscount() != null) {
            discountService.incrementUsage(discountCode);
        }
        
        // Clear the cart after order is saved
        cartService.clearCart(user);
        
        return savedOrder;
    }
    
    // Discount calculation is now handled directly in the createOrder method
    
    public Order processOrder(Long orderId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setStatus("PROCESSED");
            return orderRepository.save(order);
        }
        throw new RuntimeException("Order not found");
    }
    
    public Order completeOrder(Long orderId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setStatus("COMPLETED");
            return orderRepository.save(order);
        }
        throw new RuntimeException("Order not found");
    }
    
    public List<Order> getUserOrders(User user) {
        return orderRepository.findByUserOrderByOrderDateDesc(user);
    }
    
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }
}