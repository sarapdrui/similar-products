
package com.inditex.similarproducts.infrastructure.mapper;

import com.inditex.similarproducts.domain.Product;
import com.inditex.similarproducts.infrastructure.rest.dto.ProductDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto toDto(Product product);
}
