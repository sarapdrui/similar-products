
package com.inditex.similarproducts.application;

import com.inditex.similarproducts.domain.Product;
import com.inditex.similarproducts.domain.ProductNotFoundException;
import com.inditex.similarproducts.infrastructure.clients.ExistingApisClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class SimilarProductsService {

    private final ExistingApisClient client;
    private final boolean strictNotFound;
    private final int concurrency;
    private final int prefetch;

    public SimilarProductsService(ExistingApisClient client,
                                  @Value("${similar.strictNotFound:false}") boolean strictNotFound,
                                  @Value("${similar.concurrency:8}") int concurrency,
                                  @Value("${similar.prefetch:32}") int prefetch) {
        this.client = client;
        this.strictNotFound = strictNotFound;
        this.concurrency = concurrency;
        this.prefetch = prefetch;
    }

    public Flux<Product> findSimilar(String productId) {
        Mono<Product> base = client.getProduct(productId);

        return base.thenMany(
                client.getSimilarIds(productId)
                        .flatMapMany(Flux::fromIterable)
                        .flatMapSequential(
                                id -> client.getProduct(id)
                                        .onErrorResume(throwable -> {
                                            if (throwable instanceof ProductNotFoundException) {
                                                return strictNotFound ? Mono.error(throwable) : Mono.empty();
                                            }
                                            return Mono.empty();
                                        }),
                                concurrency,
                                prefetch
                        )
        );
    }
}
