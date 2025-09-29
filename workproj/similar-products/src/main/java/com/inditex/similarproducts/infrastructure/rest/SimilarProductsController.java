
package com.inditex.similarproducts.infrastructure.rest;

import com.inditex.similarproducts.application.SimilarProductsService;
import com.inditex.similarproducts.infrastructure.rest.dto.ProductDto;
import org.springframework.http.ResponseEntity;
import com.inditex.similarproducts.infrastructure.mapper.ProductMapper;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/product")
public class SimilarProductsController {

    private final SimilarProductsService service;
    private final ProductMapper mapper;

    public SimilarProductsController(SimilarProductsService service, ProductMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping("/{productId}/similar")
    public Mono<ResponseEntity<List<ProductDto>>> getSimilar(@PathVariable String productId) {
        return service.findSimilar(productId)
                .map(mapper::toDto)
                .collectList()
                .map(ResponseEntity::ok);
    }
}