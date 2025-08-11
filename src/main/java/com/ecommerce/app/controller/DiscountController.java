package com.ecommerce.app.controller;

import com.ecommerce.app.dto.DiscountDto;
import com.ecommerce.app.entity.Discount;
import com.ecommerce.app.mapper.DiscountMapper;
import com.ecommerce.app.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// Temporarily disabled - using ApiController instead
//@RestController
//@RequestMapping("/api/v1/discounts")
//@CrossOrigin(origins = "*")
@Deprecated
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    @Autowired
    private DiscountMapper discountMapper;

    @GetMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateDiscount(
            @RequestParam String code, 
            @RequestParam BigDecimal amount) {
        
        Map<String, Object> response = new HashMap<>();
        if (discountService.isValidDiscount(code)) {
            BigDecimal discountAmount = discountService.calculateDiscount(code, amount);
            response.put("valid", true);
            response.put("discountAmount", discountAmount);
            response.put("message", "Discount applied successfully");

            Optional<Discount> discountOpt = discountService.getDiscountByCode(code);
            discountOpt.ifPresent(discount -> {
                response.put("description", discount.getDescription());
                response.put("percentage", discount.getPercentage());
            });
        } else {
            response.put("valid", false);
            response.put("message", "Invalid or expired discount code");
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<DiscountDto>> getAllDiscounts() {
        List<Discount> discounts = discountService.getAllDiscounts();
        return ResponseEntity.ok(discountMapper.toDtoList(discounts));
    }

    @PostMapping
    public ResponseEntity<DiscountDto> createDiscount(@RequestBody DiscountDto discountDto) {
        Discount discount = discountMapper.toEntity(discountDto);
        Discount savedDiscount = discountService.saveDiscount(discount);
        return ResponseEntity.ok(discountMapper.toDto(savedDiscount));
    }
}
