package com.ecommerce.app.mapper;

import com.ecommerce.app.dto.DiscountDto;
import com.ecommerce.app.entity.Discount;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DiscountMapper {
    
    public DiscountDto toDto(Discount discount) {
        if (discount == null) {
            return null;
        }
        
        return new DiscountDto(
            discount.getId(),
            discount.getCode(),
            discount.getDescription(),
            discount.getPercentage(),
            discount.getMinimumOrderAmount(),
            discount.getMaxDiscountAmount(),
            discount.getStartDate(),
            discount.getEndDate(),
            discount.isActive(),
            discount.getUsageLimit(),
            discount.getUsageCount()
        );
    }
    
    public Discount toEntity(DiscountDto discountDto) {
        if (discountDto == null) {
            return null;
        }
        
        Discount discount = new Discount();
        discount.setId(discountDto.getId());
        discount.setCode(discountDto.getCode());
        discount.setDescription(discountDto.getDescription());
        discount.setPercentage(discountDto.getPercentage());
        discount.setMinimumOrderAmount(discountDto.getMinimumOrderAmount());
        discount.setMaxDiscountAmount(discountDto.getMaxDiscountAmount());
        discount.setStartDate(discountDto.getStartDate());
        discount.setEndDate(discountDto.getEndDate());
        discount.setActive(discountDto.isActive());
        discount.setUsageLimit(discountDto.getUsageLimit());
        discount.setUsageCount(discountDto.getUsageCount());
        
        return discount;
    }
    
    public List<DiscountDto> toDtoList(List<Discount> discounts) {
        if (discounts == null) {
            return null;
        }
        
        return discounts.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    public List<Discount> toEntityList(List<DiscountDto> discountDtos) {
        if (discountDtos == null) {
            return null;
        }
        
        return discountDtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
} 