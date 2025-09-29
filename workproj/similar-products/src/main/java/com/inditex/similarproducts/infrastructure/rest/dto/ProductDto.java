
package com.inditex.similarproducts.infrastructure.rest.dto;

public record ProductDto(
        String id,
        String name,
        double price,
        boolean availability
) {}
