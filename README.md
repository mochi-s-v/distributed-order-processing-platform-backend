# The E-Commerce Microservice Backend

This repository tracks the version history of taking a heavy, tightly knotted monolithic app and tearing it down into independent, loosely coupled **microservices**. 

---

## Project Highlights

- 8 Spring Boot Microservices
- API Gateway with JWT Authentication
- Service Discovery using Eureka
- Stripe Payment Integration
- Stripe Webhooks
- Apache Kafka Event Processing
- 3-Broker Kafka Cluster with Replication
- OpenFeign Inter-Service Communication
- Dockerized Deployment
- Independent Databases per Service
- MySQL + Spring Data JPA
- Prometheus Metrics Collection
- Grafana Monitoring Dashboards
- Distributed Tracing with Zipkin

---

## What I’ve Built So Far

### 1. Eureka Service Registry
* **What it does:** Operates on port `8761`. It acts as the registry for the entire system.
* **Why it matters:** Services don't need to know hardcoded URLs to talk to each other anymore. They register with Eureka, and look each other up dynamically.

### 2. Spring Cloud Gateway
* **What it does:** Sits out front on port `8080`. Absolutely no client talks to the backend databases directly; everything flows through the Gateway.
* **Why it matters:** It parses incoming client JWT tokens, validates them, strips them away, and forwards the clean identity headers (`X-LoggedIn-User`, `X-LoggedIn-Role`) downstream. 

### 3. Anti-Spoofing Perimeter Security
* **The Problem:** If a hacker scans the system's ports and hits a microservice directly (like hitting the Cart Service on port `8082`), they could type in fake headers like `X-LoggedIn-Role: ROLE_ADMIN` and hijack the system.
* **The Fix:** Built a custom **Zero-Trust Shield**. The Gateway automatically injects a confidential, random `X-Gateway-Secret` string to every request. 
* **The Result:** Every service verifies this secret first thing in a strict `GatewaySecretFilter`. If a request doesn't have the secret handshake, it gets rejected with a `401 Unauthorized` instantly. The system remains stateless while preventing direct service access and forged identity headers.

### 4. Decoupling by Reference
* **The Problem:** In the monolith, `CartEntity` was tightly bound to `ProductEntity` and `UserEntity`.
* **The Fix:** Decoupled that and the `CART-SERVICE` now only saves flat properties like `username` and `productId`. It handles its own business, keeping its databases completely independent.

### 5. Inter-Service Calls using OpenFeign + Interceptors
* **The Solution:** When the Cart Service needs live product prices or stock data from the Product Service, it uses declarative **OpenFeign** clients.
* **The Security:** Wrote a custom `RequestInterceptor` that automatically grabs the active security token and the secret handshake key, attaching them to outbound Feign templates so downstream services don't block the call.

### 6. Order Service & Historical Sales Ledger
* **The Problem:** Shopping carts are transient and dynamic. If a product's price or name changes in the future, past transaction records inside an old checkout could get corrupted or alter financial logs.
* **The Fix:** Created an immutable sales ledger system. The `ORDER-SERVICE` captures the checkout snapshot and maps elements into an explicit parent-child relational tree (`OrderEntity` $\rightarrow$ `OrderItemsEntity`) using a strict `CascadeType.ALL` lifecycle persistence strategy.
* **The Result:** Converts temporary shopping cart items into a permanent, unalterable historical sales record, securely baking in the exact `priceAtPurchase` state.

### 7. Payment Service & Stripe Integration
* **The Problem:** A checkout flow is incomplete without a secure mechanism to process customer payments and confirm transaction success before creating a final order.
* **The Fix:** Integrated the system with the Stripe payment gateway. The Payment Service creates payment intents, handles transaction processing, and validates successful payments before allowing checkout completion.
* **The Result:** Orders are finalized only after successful payment authorization, ensuring that unpaid checkouts cannot be converted into completed purchases.

### 8. Stripe webhook & Stripe cli
* **The problem:** After the payment url is sent to the user, There's no way for the system to know if the payment has been a success or a failure.
* **The Fix:** By taking advantage of the stripe webhook events and stripe cli. Every payment sends a response back to the system.
* **The Result:** When ever the payment url is sent to the user, the Stripe cli waits for response and when the stripe server sends a response to the system, I check for `checkout.session.completed`, `payment_intent.payment_failed` and trigger events accordingly.

### 9. Automated Stock Shrinker Logic
* **The Problem:** Race conditions could allow multiple checkouts to over-purchase an item, dropping inventory below zero.
* **The Fix:** Designed a high-integrity, transactional inventory deduction sequence inside the `PRODUCT-SERVICE`.
* **The Result:** The system explicitly checks real-time available stock against incoming requested values. If the item count is sufficient, it precisely decrements the balance (safeguarding against dropping stock to 0 on small purchases). If inventory is short, an error cascades back over the network layer via Feign exception bubbles to gracefully block and roll back the entire transaction.

### 10. Dockerized Infrastructure
* **The Problem:** Running multiple microservices and databases manually requires starting each application separately and managing networking configurations by hand.
* **The Fix:** Containerized every microservice using Docker and orchestrated the entire platform with Docker Compose.
* **The Optimization:** I also Implemented multi-stage Docker builds for every service. Maven and build dependencies exist only in the temporary build stage, while the final runtime image contains only the compiled JAR and JDK runtime.
* **The Optimization:** Also Implemented MySQL healthchecks with a start_period grace window. Dependent services only start after their database is confirmed ready to accept connections, eliminating startup race conditions.
* **The Result:** Lean container images, faster deployments and cleaner production ready services which can be initiated with a single command that also avoid race conditions.

```
Build Stage
├── Maven
├── Source Code
├── Dependencies
└── Generates JAR
          │
          ▼
Runtime Stage
├── JDK
└── Application JAR
```

### 11. Automated Data Seeding
* **The Problem:** Every fresh environment needs baseline data to be testable — an admin account, a default user, product categories, and inventory.
* **The Fix:** Implemented CommandLineRunner seeders across services. On every startup, the system checks and creates an admin account, a default user (jane) with an address, 3 product categories, and 5 products if they don't already exist.
* **The Result:** A completely fresh `docker compose up` gives you a fully populated, immediately testable environment with zero manual setup. Product stock is also restocked to 10 units on every restart, making repeated checkout testing seamless without manual DB resets.

### 12. Event-Driven Communication with Apache Kafka
* **The Problem:** Critical workflows such as inventory updates and customer notifications should continue operating even if individual consumers or brokers become unavailable.
* **The Fix:** Implemented a 3-broker Kafka cluster with a replication factor of 3 and min.insync.replicas=2 to provide fault tolerance and durable event delivery. Separate consumer groups process inventory and notification events independently, allowing multiple downstream services to react to the same business event.
* **The Result:** The platform remains resilient to broker failures while maintaining reliable event processing for stock management and customer communications.

### 13. Notification Service
* **The Problem:** Sending emails synchronously during checkout increases response times and tightly couples customer communication to the ordering workflow.
* **The Fix:** Built a dedicated Notification Service that consumes Kafka events and delivers email notifications asynchronously using SMTP through Spring Mail.
* **The Result:** Checkout requests remain fast and resilient while customer communications are processed independently in the background.

### 14. Observability & Distributed Tracing
* **The Problem:** As the number of services grows, debugging failures and understanding request flow across service boundaries becomes increasingly difficult.
* **The Fix:** Integrated Prometheus for metrics collection, Grafana for visualization, and Zipkin for distributed tracing across all microservices. Micrometer helps trace request between services and Kafka-driven workflows.
* **The Result:** End-to-end request visibility, centralized monitoring dashboards, JVM metrics, service health tracking, and trace-based debugging across the platform.

---

## The Blueprint

<img width="1516" height="1249" alt="Screenshot 2026-06-03 015344" src="https://github.com/user-attachments/assets/f6ab2848-cd3f-49e6-a6ae-d5a15071210e" />

---

## Checkout Workflow

```
## Checkout & Order Processing Flow

User
│
▼
API Gateway
│
├── JWT Validation
└── Route Request
│
▼
Cart Service
│
└── Checkout Request
│
▼
Payment Service
│
├── Create Stripe Checkout Session
└── Return Payment URL
│
▼
Stripe Checkout
│
└── Customer Completes Payment
│
▼
Stripe Webhook
│
└── Payment Success Event
│
▼
Kafka Cluster
│
├── 3 Brokers
├── Replication Factor = 3
└── Min ISR = 2
│
├───────────────────┬───────────────────┐
│                   │                   │
▼                   ▼                   ▼
Order            Product            Notification
Service          Service            Service
│                   │                   │
├── Save            ├── Reduce          └── Send
│   Order           │   Stock               Email
│                   │
▼                   ▼
──────────Order Completed──────────
```

---

## Pre requisites to run the application

1. Create a stripe test account
2. Create a .env file and use the .env.example file to create environment variables
3. There are 5 environment variables :
```
# Stripe
STRIPE_SECRET_KEY="your stripe secret key"
STRIPE_WEBHOOK_KEY="your stripe webhook key"

# Databases
SPRING_DATASOURCE_PASSWORD="your docker mysql password (default one : my-secret-pw)"

# Gateway
SPRING_GATEWAY_SECRET="your gateway secret key"

#JWT
JWT_SIGNIN_KEY="your jwt sign in key"

# Gmail
GMAIL_APP_PASSWORD="your gmail app password"
GMAIL_USERNAME="your gmail id"
```
---

## Running with Docker
```
# Build and start all services in detached mode
docker-compose up --build --detach

# Stop everything
docker-compose down
```
<img width="1958" height="892" alt="Screenshot 2026-06-20 000941" src="https://github.com/user-attachments/assets/a1d9c68c-e94c-4b39-ab0b-e70f9e5fe92c" />

---

## Service Endpoints

### Core Services

| Service          | URL                   |
| ---------------- | --------------------- |
| API Gateway      | http://localhost:8080 |
| Eureka Dashboard | http://localhost:8761 |

### Observability Stack

| Tool       | URL                   | Purpose                                           |
| ---------- | --------------------- | ------------------------------------------------- |
| Kafka UI   | http://localhost:8085 | Kafka topic, broker and consumer group monitoring |
| Prometheus | http://localhost:9090 | Metrics collection and querying                   |
| Grafana    | http://localhost:3000 | Metrics visualization and dashboards              |
| Zipkin     | http://localhost:9411 | Distributed tracing and request flow analysis     |

### Default Grafana Credentials

```text
Username: admin
Password: admin
```

---

## APIs

<img width="1054" height="545" alt="image (2)" src="https://github.com/user-attachments/assets/d0363437-5e1b-4a2d-8442-8027ab4f5d87" />

---

## What's Next on the Roadmap?

Here is what I'm building next to take this platform to production level:

* **Keycloak OAuth2:** Centralized IAM replacing custom filters with industry-standard Single Sign-On (SSO) and token validation.
* **Resilience4j:** Safety nets for Feign clients. Trips open if a service fails, serving cached/fallback data instead of crashing.
* **Saga Orchestration:** Distributed transaction engine that auto-rolls back changes across services if a checkout step fails.

---

## The Tech Stack

* **Language:** Java 17 / 21
* **Core Framework:** Spring Boot 3.x
* **Security:** Spring Security 6.x & JWT
* **Service Discovery:** Spring Cloud Netflix Eureka
* **API Gateway:** Spring Cloud Gateway
* **Payment Gateway:** Stripe Payment Gateway, Stripe CLI, Stripe Webhooks
* **Event Streaming:** Apache Kafka
* **Email Notifications:** Spring Mail & SMTP
* **Inter-Service Communication:** Spring Cloud OpenFeign
* **Database & ORM:** MySQL & Spring Data JPA (Hibernate)
* **DevOps:** Docker, Docker Compose, Multi-stage Docker Builds
* **Monitoring:** Prometheus, Grafana
* **Distributed Tracing:** Zipkin, Micrometer Tracing
* **Boilerplate Reduction:** Lombok
