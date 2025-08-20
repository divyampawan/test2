package com.ecommerce.app.mapper;

import com.ecommerce.app.dto.DiscountDto;
import com.ecommerce.app.dto.OrderItemDto;
import com.ecommerce.app.entity.Discount;
import com.ecommerce.app.entity.OrderItem;
import com.ecommerce.app.mapper.ProductMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.List;


@Mapper(componentModel = "spring")
public interface DiscountMapper {

    DiscountDto toDto(Discount discount);

    Discount toEntity(DiscountDto discountDto);

    List<DiscountDto> toDtoList(List<Discount> discounts);

    List<Discount> toEntityList(List<DiscountDto> discountDtos);
}