package com.ecommerce.app.mapper;

import com.ecommerce.app.dto.OrderDto;
import com.ecommerce.app.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Mapper(componentModel = "spring")
public abstract class OrderMapper {

    @Autowired
    protected OrderItemMapper orderItemMapper;
    
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "discountId", source = "discount.id")
    @Mapping(target = "orderItems", expression = "java(mapOrderItems(order.getOrderItems()))")
    public abstract OrderDto toDto(Order order);
    
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "discount", ignore = true)
    @Mapping(target = "orderItems", expression = "java(mapOrderItemDtos(orderDto.getOrderItems()))")
    public abstract Order toEntity(OrderDto orderDto);
    
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
    
    protected List<com.ecommerce.app.dto.OrderItemDto> mapOrderItems(List<com.ecommerce.app.entity.OrderItem> orderItems) {
        if (orderItems == null) {
            return null;
        }
        return orderItems.stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toList());
    }
    
    protected List<com.ecommerce.app.entity.OrderItem> mapOrderItemDtos(List<com.ecommerce.app.dto.OrderItemDto> orderItemDtos) {
        if (orderItemDtos == null) {
            return null;
        }
        return orderItemDtos.stream()
                .map(orderItemMapper::toEntity)
                .collect(Collectors.toList());
    }
}