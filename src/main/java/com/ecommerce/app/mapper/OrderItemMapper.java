package com.ecommerce.app.mapper;

import com.ecommerce.app.dto.OrderItemDto;
import com.ecommerce.app.entity.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderItemMapper {
    
    public OrderItemDto toDto(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }
        
        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setId(orderItem.getId());
        orderItemDto.setQuantity(orderItem.getQuantity());
        orderItemDto.setPrice(orderItem.getPrice());
        orderItemDto.setTotalPrice(orderItem.getTotalPrice());
        
        if (orderItem.getProduct() != null) {
            orderItemDto.setProductId(orderItem.getProduct().getId());
            orderItemDto.setProductName(orderItem.getProduct().getName());
        }
        
        return orderItemDto;
    }
    
    public OrderItem toEntity(OrderItemDto orderItemDto) {
        if (orderItemDto == null) {
            return null;
        }
        
        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(orderItemDto.getQuantity());
        orderItem.setPrice(orderItemDto.getPrice());
        orderItem.setTotalPrice(orderItemDto.getTotalPrice());
        
        return orderItem;
    }
    
    public List<OrderItemDto> toDtoList(List<OrderItem> orderItems) {
        if (orderItems == null) {
            return null;
        }
        
        return orderItems.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    public List<OrderItem> toEntityList(List<OrderItemDto> orderItemDtos) {
        if (orderItemDtos == null) {
            return null;
        }
        
        return orderItemDtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
} 