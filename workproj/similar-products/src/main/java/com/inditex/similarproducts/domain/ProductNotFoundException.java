
package com.inditex.similarproducts.domain;

public class ProductNotFoundException extends RuntimeException {
    private final String productId;
    public ProductNotFoundException(String productId) {
        super("Product %s not found".formatted(productId));
        this.productId = productId;
    }
    public String getProductId() { return productId; }
}
