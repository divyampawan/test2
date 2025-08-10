package com.ecommerce.app.mapper;

import com.ecommerce.app.dto.CartItemDto;
import com.ecommerce.app.entity.CartItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartItemMapper {
    
    public CartItemDto toDto(CartItem cartItem) {
        if (cartItem == null) {
            return null;
        }
        
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setId(cartItem.getId());
        cartItemDto.setQuantity(cartItem.getQuantity());
        
        if (cartItem.getProduct() != null) {
            cartItemDto.setProductId(cartItem.getProduct().getId());
            cartItemDto.setProductName(cartItem.getProduct().getName());
            cartItemDto.setProductPrice(cartItem.getProduct().getPrice().doubleValue());
            cartItemDto.setSubtotal(cartItem.getProduct().getPrice().multiply(new BigDecimal(cartItem.getQuantity())).doubleValue());
        }
        
        return cartItemDto;
    }
    
    public List<CartItemDto> toDtoList(List<CartItem> cartItems) {
        if (cartItems == null) {
            return null;
        }
        
        return cartItems.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
} 