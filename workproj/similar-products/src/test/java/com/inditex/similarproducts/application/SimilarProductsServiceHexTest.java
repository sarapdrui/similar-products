
package com.inditex.similarproducts.application;

import com.inditex.similarproducts.domain.Product;
import com.inditex.similarproducts.domain.ProductNotFoundException;
import com.inditex.similarproducts.infrastructure.clients.ExistingApisClient;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.*;

public class SimilarProductsServiceHexTest {


    @Test
    void returnsOrdered_and_skipsNotFoundAndTimeouts() {
        ExistingApisClient client = mock(ExistingApisClient.class);
        // base exists
        when(client.getProduct("2")).thenReturn(Mono.just(new Product("2","Dress",19.99,true)));
        // similar ids
        when(client.getSimilarIds("2")).thenReturn(Mono.just(List.of("3","100","1000")));
        // product 3 ok
        when(client.getProduct("3")).thenReturn(Mono.just(new Product("3","Blazer",29.99,false)));
        // product 100 ok (simulate slower but ok: just return Mono.just)
        when(client.getProduct("100")).thenReturn(Mono.just(new Product("100","Trousers",49.99,false)));
        // product 1000 timeout/5xx simulated by generic error
        when(client.getProduct("1000")).thenReturn(Mono.error(new IllegalStateException("timeout")));

        SimilarProductsService svc = new SimilarProductsService(client, false, 8, 32);

        StepVerifier.create(svc.findSimilar("2"))
                .expectNextMatches(p -> p.id().equals("3"))
                .expectNextMatches(p -> p.id().equals("100"))
                .verifyComplete();
    }
}
