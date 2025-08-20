package com.ecommerce.app.controller;

import com.ecommerce.app.dto.DiscountDto;
import com.ecommerce.app.entity.Discount;
import com.ecommerce.app.mapper.DiscountMapper;
import com.ecommerce.app.service.interfaces.IDiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/discounts")
@CrossOrigin(origins = "*")
public class DiscountController {

    private final IDiscountService discountService;
    private final DiscountMapper discountMapper;

    @Autowired
    public DiscountController(IDiscountService discountService, DiscountMapper discountMapper) {
        this.discountService = discountService;
        this.discountMapper = discountMapper;
    }

    @GetMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateDiscount(
            @RequestParam String code, 
            @RequestParam BigDecimal amount) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (discountService.isValidDiscount(code)) {
                BigDecimal discountAmount = discountService.calculateDiscount(code, amount);
                response.put("valid", true);
                response.put("discountAmount", discountAmount);
                response.put("message", "Discount applied successfully");

                // Get discount details
                Optional<Discount> discountOpt = discountService.getDiscountByCode(code);
                if (discountOpt.isPresent()) {
                    Discount discount = discountOpt.get();
                    response.put("description", discount.getDescription());
                    response.put("percentage", discount.getPercentage());
                }
            } else {
                response.put("valid", false);
                response.put("message", "Invalid or expired discount code");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("valid", false);
            response.put("message", "Error validating discount: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<List<DiscountDto>> getAllDiscounts() {
        try {
            List<Discount> discounts = discountService.getAllDiscounts();
            return ResponseEntity.ok(discountMapper.toDtoList(discounts));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<DiscountDto> createDiscount(@RequestBody DiscountDto discountDto) {
        try {
            Discount discount = discountMapper.toEntity(discountDto);
            Discount savedDiscount = discountService.saveDiscount(discount);
            return ResponseEntity.ok(discountMapper.toDto(savedDiscount));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
