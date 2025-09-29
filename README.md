# Similar Products API

This project implements the **technical test** for Inditex:  
a Spring Boot WebFlux application exposing a REST API to retrieve **similar products** for a given one.

## ✈️ Overview

We provide a new feature to show customers **similar products** to the one they are currently viewing.  
The API orchestrates existing upstream endpoints to return product details of similar items.

Architecture highlights:

- **Spring Boot 3 + WebFlux (Reactive)**.
- **Hexagonal / Clean Architecture** separation:
  - Domain model (`Product`).
  - Application service (`SimilarProductsService`).
  - Infrastructure adapters:
    - REST controller.
    - External API client (`ExistingApisClient`).
  - DTOs + MapStruct mapper for API layer.
- **Resilience** with Resilience4j (`TimeLimiter`, `Retry`, `CircuitBreaker`).

## 🖥️ Running the application

Make sure you have **Java 21** and **Docker** installed.
