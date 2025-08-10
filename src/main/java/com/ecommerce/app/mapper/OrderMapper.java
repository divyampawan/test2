package com.ecommerce.app.mapper;

import com.ecommerce.app.dto.OrderDto;
import com.ecommerce.app.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {
    
    @Autowired
    private OrderItemMapper orderItemMapper;
    
    public OrderDto toDto(Order order) {
        if (order == null) {
            return null;
        }
        
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setOrderDate(order.getOrderDate());
        orderDto.setStatus(order.getStatus());
        orderDto.setSubtotal(order.getSubtotal());
        orderDto.setTotalAmount(order.getTotalAmount());
        orderDto.setDiscountCode(order.getDiscountCode());
        orderDto.setDiscountAmount(order.getDiscountAmount());
        
        if (order.getDiscount() != null) {
            orderDto.setDiscountId(order.getDiscount().getId());
        }
        
        if (order.getUser() != null) {
            orderDto.setUserId(order.getUser().getId());
            orderDto.setUsername(order.getUser().getUsername());
        }
        
        if (order.getOrderItems() != null) {
            orderDto.setOrderItems(orderItemMapper.toDtoList(order.getOrderItems()));
        }
        
        return orderDto;
    }
    
    public Order toEntity(OrderDto orderDto) {
        if (orderDto == null) {
            return null;
        }
        
        Order order = new Order();
        order.setOrderDate(orderDto.getOrderDate());
        order.setStatus(orderDto.getStatus());
        order.setSubtotal(orderDto.getSubtotal());
        order.setTotalAmount(orderDto.getTotalAmount());
        order.setDiscountCode(orderDto.getDiscountCode());
        order.setDiscountAmount(orderDto.getDiscountAmount());
        
        if (orderDto.getOrderItems() != null) {
            order.setOrderItems(orderItemMapper.toEntityList(orderDto.getOrderItems()));
        }
        
        return order;
    }
    
    public List<OrderDto> toDtoList(List<Order> orders) {
        if (orders == null) {
            return null;
        }
        
        return orders.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    public List<Order> toEntityList(List<OrderDto> orderDtos) {
        if (orderDtos == null) {
            return null;
        }
        
        return orderDtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}