package com.ecommerce.app.mapper;

import com.ecommerce.app.dto.CartItemDto;
import com.ecommerce.app.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface CartItemMapper {

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.imageUrl", target = "productImageUrl")
    @Mapping(target = "productPrice", expression = "java(cartItem.getProduct() != null ? cartItem.getProduct().getPrice().doubleValue() : 0.0)")
    @Mapping(target = "subtotal", expression = "java(calculateSubtotal(cartItem))")
    CartItemDto toDto(CartItem cartItem);

    List<CartItemDto> toDtoList(List<CartItem> cartItems);

    @Named("calculateSubtotal")
    default Double calculateSubtotal(CartItem cartItem) {
        if (cartItem == null || cartItem.getProduct() == null) {
            return 0.0;
        }
        BigDecimal price = cartItem.getProduct().getPrice();
        int quantity = cartItem.getQuantity();
        return price.multiply(BigDecimal.valueOf(quantity)).doubleValue();
    }
}