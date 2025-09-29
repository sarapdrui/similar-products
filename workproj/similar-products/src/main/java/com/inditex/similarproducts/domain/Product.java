
package com.inditex.similarproducts.domain;

public record Product(
        String id,
        String name,
        double price,
        boolean availability
) {}
