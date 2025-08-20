package com.ecommerce.app.service;

import com.ecommerce.app.entity.Discount;
import com.ecommerce.app.repository.DiscountRepository;
import com.ecommerce.app.service.interfaces.IDiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class DiscountService implements IDiscountService {
    
    @Autowired
    private DiscountRepository discountRepository;
    
    public List<Discount> getAllDiscounts() {
        return discountRepository.findAll();
    }
    
    public Optional<Discount> getDiscountById(Long id) {
        return discountRepository.findById(id);
    }
    
    public Optional<Discount> getDiscountByCode(String code) {
        return discountRepository.findByCodeAndActiveTrue(code);
    }
    
    public Discount saveDiscount(Discount discount) {
        return discountRepository.save(discount);
    }
    
    public void deleteDiscount(Long id) {
        discountRepository.deleteById(id);
    }
    
    public BigDecimal calculateDiscount(String code, BigDecimal orderAmount) {
        Optional<Discount> discountOpt = getDiscountByCode(code);
        if (discountOpt.isPresent()) {
            Discount discount = discountOpt.get();
            return discount.calculateDiscount(orderAmount);
        }
        return BigDecimal.ZERO;
    }
    
    public boolean isValidDiscount(String code) {
        Optional<Discount> discountOpt = getDiscountByCode(code);
        return discountOpt.isPresent() && discountOpt.get().isValid();
    }
    
    public void incrementUsage(String code) {
        Optional<Discount> discountOpt = getDiscountByCode(code);
        if (discountOpt.isPresent()) {
            Discount discount = discountOpt.get();
            discount.incrementUsage();
            discountRepository.save(discount);
        }
    }
} 