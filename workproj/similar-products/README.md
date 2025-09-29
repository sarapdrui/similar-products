
# Similar Products API

Spring Boot 3 (WebFlux) service exposing `GET /product/{productId}/similar` on port **5000** and consuming mocks on **3001**.

## Run locally
1. Start mocks from the exercise:
   ```bash
   docker-compose up -d simulado influxdb grafana
   ```
2. Start the app:
   ```bash
   ./gradlew bootRun
   ```
3. Try it:
   ```bash
   curl http://localhost:5000/product/1/similar
   ```

## Docker
```bash
docker build -t similar-products .
docker run --rm -p 5000:5000 --network host similar-products
```

## Notes
- Parallel fetching (concurrency=8) of similar product details.
- Resilience via resilience4j (timeouts, retries, circuit breaker).
- 404 propagated if base product doesn't exist.


## Hexagonal structure update
- Domain entity: `Product`.
- API DTO: `ProductDto`.
- Mapper: MapStruct `ProductMapper` (componentModel=spring).
- Controller returns DTOs mapped from domain.

## Tests
- `SimilarProductsServiceHexTest` (Mockito + Reactor StepVerifier).
- `SimilarProductsControllerHexTest` (WebFluxTest + mocked mapper).
