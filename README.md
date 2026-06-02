# 🛒 The E-Commerce Microservice Backend

This repository tracks the version history of taking a heavy, tightly knotted monolithic app and tearing it down into lean, independent **Microservices**. 

---

## 🏗️ What I’ve Built So Far

### 🗺️ 1. Eureka Service Registry
* **What it does:** Operates on port `8761`. It acts as the registry for our entire system.
* **Why it's cool:** Services don't need to know hardcoded URLs to talk to each other anymore. They register with Eureka, and look each other up dynamically.

### 🛡️ 2. Spring Cloud Gateway
* **What it does:** Sits out front on port `8080`. Absolutely no client talks to our backend databases directly; everything flows through the Gateway.
* **Why it's cool:** It parses incoming client JWT tokens, validates them, strips them away, and forwards the clean identity headers (`X-LoggedIn-User`, `X-LoggedIn-Role`) downstream. 

### 🥷 3. Anti-Spoofing Perimeter Security
* **The Problem:** If a hacker scans our ports and hits a microservice directly (like hitting the Cart Service on port `8082`), they could type in fake headers like `X-LoggedIn-Role: ROLE_ADMIN` and hijack the system.
* **The Fix:** I built a custom **Zero-Trust Shield**. The Gateway automatically injects a confidential, random `X-Gateway-Secret` string to every request. 
* **The Result:** Every service verifies this secret first thing in a strict `GatewaySecretFilter`. If a request doesn't have the secret handshake, it gets slapped with a `401 Unauthorized` instantly. Yes, this makes the system **stateful** but in turn makes it very secure.

### ✂️ 4. Decoupling by Reference
* **The Problem:** In the monolith, `CartEntity` was tightly bound to `ProductEntity` and `UserEntity`.
* **The Fix:** I decoupled that. The `CART-SERVICE` now only saves flat properties like `username` and `productId`. It handles its own business, keeping its databases completely independent.

### 📞 5. Inter-Service Calls using OpenFeign + Interceptors
* **The Solution:** When the Cart Service needs live product prices or stock data from the Product Service, it uses declarative **OpenFeign** clients.
* **The Security:** I wrote a custom `RequestInterceptor` that automatically grabs the active security token and the secret handshake key, attaching them to outbound Feign templates so downstream services don't block the call.
  
---

## 📊 The Blueprint
<img width="1516" height="1249" alt="Screenshot 2026-06-03 015344" src="https://github.com/user-attachments/assets/f6ab2848-cd3f-49e6-a6ae-d5a15071210e" />

---

## 🔮 What's Next on the Roadmap?

Here is what I'm building next to take this platform to production level:

* **📦 The Order Service:** A stateful tracking system that takes runtime carts and locks them into permanent, immutable historical ledgers (`priceAtPurchase`).
* **⚖️ The Stock Shrinker:** An automated stock subtraction workflow that forces the Order Service to verify and permanently decrement available product catalog counts mid-checkout.
* **🎪 Saga Orchestration:** If a checkout fails halfway through (e.g., payment fails after stock is deducted), a distributed compensation engine automatically rolls back data changes across services.
* **🚀 Apache Kafka Streaming:** Dropping heavy synchronous processing loops for lightning-fast asynchronous events (e.g., throwing a `CheckoutEvent` into Kafka so an independent Notification Service can handle emails in the background).
* **🛡️ Fault Tolerance (Resilience4j Circuit Breaker):** Wrapping our OpenFeign clients inside safety nets. If the Product Service goes down temporarily, the Circuit Breaker trips open, allowing the Cart Service to gracefully return fallback/cached data instead of throwing a massive error page at our users.
