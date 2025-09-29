
package com.inditex.similarproducts.infrastructure.rest;

import com.inditex.similarproducts.domain.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleNotFound(ProductNotFoundException ex) {
        return Map.of(
                "type", "about:blank",
                "title", "Product Not found",
                "status", 404,
                "detail", ex.getMessage()
        );
    }
}
