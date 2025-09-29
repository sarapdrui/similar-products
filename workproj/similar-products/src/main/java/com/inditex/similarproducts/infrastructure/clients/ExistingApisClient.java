
package com.inditex.similarproducts.infrastructure.clients;

import com.inditex.similarproducts.domain.Product;
import com.inditex.similarproducts.domain.ProductNotFoundException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class ExistingApisClient {

    private final WebClient client;
    private static final Logger log = LoggerFactory.getLogger(ExistingApisClient.class);


    public ExistingApisClient(@Value("${similar.upstream.base-url}") String baseUrl,
                              WebClient.Builder builder) {
        this.client = builder.baseUrl(baseUrl).build();
    }

    @TimeLimiter(name = "existingApis")
    @Retry(name = "existingApis")
    @CircuitBreaker(name = "existingApis")
    public Mono<List<String>> getSimilarIds(String productId) {
        return client.get().uri("/product/{id}/similarids", productId)
                .retrieve()
                .bodyToFlux(Object.class)
                .map(Object::toString)
                .collectList();
    }


    @TimeLimiter(name = "existingApis")
    @Retry(name = "existingApis")
    @CircuitBreaker(name = "existingApis")
    public Mono<Product> getProduct(String productId) {
        return client.get().uri("/product/{id}", productId)
                .exchangeToMono(resp -> {
                    if (resp.statusCode().is2xxSuccessful()) {
                        return resp.bodyToMono(Product.class);
                    } else if (resp.statusCode().value() == HttpStatus.NOT_FOUND.value()) {
                        return Mono.error(new ProductNotFoundException(productId));
                    } else {
                        return Mono.error(new IllegalStateException("Upstream error: " + resp.statusCode()));
                    }
                });
    }
}
