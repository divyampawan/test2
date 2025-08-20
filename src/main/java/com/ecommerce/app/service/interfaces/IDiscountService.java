package com.ecommerce.app.service.interfaces;

import com.ecommerce.app.entity.Discount;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface IDiscountService {
    List<Discount> getAllDiscounts();
    Optional<Discount> getDiscountById(Long id);
    Optional<Discount> getDiscountByCode(String code);
    Discount saveDiscount(Discount discount);
    void deleteDiscount(Long id);
    BigDecimal calculateDiscount(String code, BigDecimal orderAmount);
    boolean isValidDiscount(String code);
    void incrementUsage(String code);
}
