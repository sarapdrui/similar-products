
package com.inditex.similarproducts.infrastructure.rest;

import com.inditex.similarproducts.application.SimilarProductsService;
import com.inditex.similarproducts.domain.Product;
import com.inditex.similarproducts.infrastructure.mapper.ProductMapper;
import com.inditex.similarproducts.infrastructure.rest.dto.ProductDto;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

@WebFluxTest(controllers = SimilarProductsController.class)
class SimilarProductsControllerHexTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    SimilarProductsService service;

    @MockBean
    ProductMapper mapper;

    @Test
    void controllerMapsAndReturnsDtos() {
        Mockito.when(service.findSimilar("1")).thenReturn(Flux.just(
                new Product("2","Dress",19.99,true),
                new Product("3","Blazer",29.99,false)
        ));
        Mockito.when(mapper.toDto(Mockito.any(Product.class)))
                .thenAnswer(inv -> {
                    Product p = inv.getArgument(0);
                    return new ProductDto(p.id(), p.name(), p.price(), p.availability());
                });

        webTestClient.get().uri("/product/1/similar")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$[0].id").isEqualTo("2")
                .jsonPath("$[1].name").isEqualTo("Blazer");
    }
}
