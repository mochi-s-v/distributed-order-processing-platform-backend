# 🛒 The E-Commerce Microservice Backend

This repository tracks the version history of taking a heavy, tightly knotted monolithic app and tearing it down into lean, independent **Microservices**. 

---

## 🏗️ What I’ve Built So Far

### 🗺️ 1. Eureka Service Registry
* **What it does:** Operates on port `8761`. It acts as the registry for the entire system.
* **Why it's cool:** Services don't need to know hardcoded URLs to talk to each other anymore. They register with Eureka, and look each other up dynamically.

### 🛡️ 2. Spring Cloud Gateway
* **What it does:** Sits out front on port `8080`. Absolutely no client talks to the backend databases directly; everything flows through the Gateway.
* **Why it's cool:** It parses incoming client JWT tokens, validates them, strips them away, and forwards the clean identity headers (`X-LoggedIn-User`, `X-LoggedIn-Role`) downstream. 

### 🥷 3. Anti-Spoofing Perimeter Security
* **The Problem:** If a hacker scans the system's ports and hits a microservice directly (like hitting the Cart Service on port `8082`), they could type in fake headers like `X-LoggedIn-Role: ROLE_ADMIN` and hijack the system.
* **The Fix:** I built a custom **Zero-Trust Shield**. The Gateway automatically injects a confidential, random `X-Gateway-Secret` string to every request. 
* **The Result:** Every service verifies this secret first thing in a strict `GatewaySecretFilter`. If a request doesn't have the secret handshake, it gets slapped with a `401 Unauthorized` instantly. Yes, this makes the system **stateful** but in turn makes it very secure.

### ✂️ 4. Decoupling by Reference
* **The Problem:** In the monolith, `CartEntity` was tightly bound to `ProductEntity` and `UserEntity`.
* **The Fix:** I decoupled that. The `CART-SERVICE` now only saves flat properties like `username` and `productId`. It handles its own business, keeping its databases completely independent.

### 📞 5. Inter-Service Calls using OpenFeign + Interceptors
* **The Solution:** When the Cart Service needs live product prices or stock data from the Product Service, it uses declarative **OpenFeign** clients.
* **The Security:** I wrote a custom `RequestInterceptor` that automatically grabs the active security token and the secret handshake key, attaching them to outbound Feign templates so downstream services don't block the call.

### 📦 6. Order Service & Historical Sales Ledger
* **The Problem:** Shopping carts are transient and dynamic. If a product's price or name changes in the future, past transaction records inside an old checkout could get corrupted or alter financial logs.
* **The Fix:** Created an immutable sales ledger system. The `ORDER-SERVICE` captures the checkout snapshot and maps elements into an explicit parent-child relational tree (`OrderEntity` $\rightarrow$ `OrderItemsEntity`) using a strict `CascadeType.ALL` lifecycle persistence strategy.
* **The Result:** Converts temporary shopping cart items into a permanent, unalterable historical sales record, securely baking in the exact `priceAtPurchase` state.

### ⚖️ 7. Automated Stock Shrinker Logic
* **The Problem:** Race conditions could allow multiple checkouts to over-purchase an item, dropping inventory below zero.
* **The Fix:** I designed a high-integrity, transactional inventory deduction sequence inside the `PRODUCT-SERVICE`.
* **The Result:** The system explicitly checks real-time available stock against incoming requested values. If the item count is sufficient, it precisely decrements the balance (safeguarding against dropping stock to 0 on small purchases). If inventory is short, an error cascades back over the network layer via Feign exception bubbles to gracefully block and roll back the entire transaction.
  
---

## 📊 The Blueprint

<img width="1516" height="1249" alt="Screenshot 2026-06-03 015344" src="https://github.com/user-attachments/assets/f6ab2848-cd3f-49e6-a6ae-d5a15071210e" />

---

## 🔗 APIs

<img width="1054" height="545" alt="image (2)" src="https://github.com/user-attachments/assets/d0363437-5e1b-4a2d-8442-8027ab4f5d87" />

---

## 🔮 What's Next on the Roadmap?

Here is what I'm building next to take this platform to production level:

* **💰 Payment Service:** A microservice responsible for processing payments during checkout. Once the payment is successfully completed, the checkout process is finalized.
* **🚀 Apache Kafka:** Asynchronous event broker to offload heavy processing (like sending confirmation emails) to background workers.
* **🐳 Docker Compose:** Packages all apps and databases so the entire platform boots with a single `docker-compose up`.
* **🔑 Keycloak OAuth2:** Centralized IAM replacing custom filters with industry-standard Single Sign-On (SSO) and token validation.
* **🎪 Saga Orchestration:** Distributed transaction engine that auto-rolls back changes across services if a checkout step fails.
* **📊 Prometheus & Grafana:** Real-time production telemetry monitoring JVM health, response times, and system error rates.
* **🛡️ Resilience4j:** Safety nets for Feign clients. Trips open if a service fails, serving cached/fallback data instead of crashing.

---

## 🛠️ The Tech Stack

* **Language:** Java 17 / 21
* **Core Framework:** Spring Boot 3.x
* **Security:** Spring Security 6.x & JWT
* **Service Discovery:** Spring Cloud Netflix Eureka
* **API Gateway:** Spring Cloud Gateway
* **Inter-Service Communication:** Spring Cloud OpenFeign
* **Database & ORM:** MySQL & Spring Data JPA (Hibernate)
* **Boilerplate Reduction:** Lombok
