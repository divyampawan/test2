package com.ecommerce.app.service.interfaces;

import com.ecommerce.app.entity.Order;
import com.ecommerce.app.entity.User;

import java.util.List;
import java.util.Optional;

public interface IOrderService {
    Order createOrder(User user, String discountCode);
    Order processOrder(Long orderId);
    Order completeOrder(Long orderId);
    List<Order> getUserOrders(User user);
    Optional<Order> getOrderById(Long id);
}
