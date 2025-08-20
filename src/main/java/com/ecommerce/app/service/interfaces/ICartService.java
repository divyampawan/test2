package com.ecommerce.app.service.interfaces;

import com.ecommerce.app.entity.CartItem;
import com.ecommerce.app.entity.User;

import java.math.BigDecimal;
import java.util.List;

public interface ICartService {
    void addToCart(User user, Long productId, int quantity);
    List<CartItem> getCartItems(User user);
    void removeFromCart(Long cartItemId);
    void updateQuantity(Long cartItemId, int quantity);
    void clearCart(User user);
    BigDecimal getCartTotal(User user);
}
