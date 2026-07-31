\# 02 - System Architecture



\# Logistics Management System



\*\*Architecture Style:\*\* Microservices Architecture



\*\*Communication:\*\*



\* REST APIs (Synchronous)

\* RabbitMQ (Asynchronous)



\*\*Authentication:\*\*



\* JWT Authentication

\* Refresh Tokens



\*\*Service Discovery:\*\*



\* Eureka Server



\*\*API Gateway:\*\*



\* Spring Cloud Gateway



\*\*Databases:\*\*



\* Database per Service



\---



\# 1. Architecture Overview



The Logistics Management System follows the \*\*Microservices Architecture\*\* pattern. Each business capability is implemented as an independent service with its own database, allowing services to be developed, deployed, and scaled independently.



The system uses:



\* Spring Cloud Gateway as the single entry point.

\* Eureka Server for service discovery.

\* JWT for authentication and authorization.

\* RabbitMQ for asynchronous communication.

\* MySQL databases dedicated to each service.

\* Docker Compose for local orchestration.

\* AWS EC2 for deployment.



\---



\# 2. High-Level Architecture



```text

&#x20;                               +----------------------+

&#x20;                               |    React Frontend    |

&#x20;                               +----------+-----------+

&#x20;                                          |

&#x20;                                          |

&#x20;                                          v

&#x20;                             +--------------------------+

&#x20;                             |   Spring Cloud Gateway   |

&#x20;                             +------------+-------------+

&#x20;                                          |

&#x20;                                          |

&#x20;                             JWT Validation \& Routing

&#x20;                                          |

&#x20;                                          |

&#x20;                   +----------------------+----------------------+

&#x20;                   |                      |                      |

&#x20;                   |                      |                      |

&#x20;                   v                      v                      v

&#x20;           +--------------+      +---------------+      +----------------+

&#x20;           | Auth Service |      | Order Service |      | Payment Service|

&#x20;           +------+-------+      +-------+-------+      +--------+-------+

&#x20;                  |                      |                       |

&#x20;                  |                      |                       |

&#x20;            auth\_db               order\_db                payment\_db

&#x20;                  |                      |                       |

&#x20;                  +----------------------+-----------------------+

&#x20;                                         |

&#x20;                                         |

&#x20;                                   RabbitMQ Events

&#x20;                                         |

&#x20;                                         |

&#x20;                                         v

&#x20;                             +-------------------------+

&#x20;                             | Notification Service    |

&#x20;                             +------------+------------+

&#x20;                                          |

&#x20;                                          |

&#x20;                                 notification\_db



&#x20;                        +---------------------------+

&#x20;                        |      Eureka Server        |

&#x20;                        +---------------------------+

&#x20;                          (Service Discovery)

```



\---



\# 3. Microservices



The project consists of six independent services.



| Service              | Responsibility                 |

| -------------------- | ------------------------------ |

| Eureka Server        | Service Discovery              |

| API Gateway          | Routing \& Security             |

| Auth Service         | Authentication \& Authorization |

| Order Service        | Orders \& Complaints            |

| Payment Service      | Payments                       |

| Notification Service | Notifications                  |



\---



\# 4. Service Responsibilities



\## 4.1 Eureka Server



\### Purpose



Acts as the service registry where all microservices register themselves.



\### Responsibilities



\* Service Registration

\* Service Discovery

\* Health Monitoring



\### Does Not



\* Process business logic

\* Authenticate users



\---



\## 4.2 API Gateway



The Gateway is the only public entry point for clients.



\### Responsibilities



\* Request Routing

\* JWT Validation

\* Forward Requests

\* Centralized Logging

\* CORS Configuration



\### Benefits



\* Single Entry Point

\* Improved Security

\* Simplified Client Communication

\* Easier Monitoring



\---



\## 4.3 Auth Service



Responsible for identity management.



\### Owns



\* Users

\* Roles

\* JWT Tokens

\* Refresh Tokens



\### APIs



\* Register

\* Login

\* Refresh Token

\* Logout

\* User Profile



\### Database



```

auth\_db

```



\### Never Owns



\* Orders

\* Payments

\* Notifications



\---



\## 4.4 Order Service



Responsible for logistics operations.



\### Owns



\* Orders

\* Complaints

\* Agent Assignments



\### APIs



\* Create Order

\* View Orders

\* Assign Agent

\* Update Status

\* Raise Complaint



\### Database



```

order\_db

```



\### Never Owns



\* Users

\* Authentication

\* Payments



\---



\## 4.5 Payment Service



Responsible for payment processing.



\### Owns



\* Payments

\* Transactions



\### Integrates With



\* Cashfree Payment Gateway



\### APIs



\* Create Payment

\* Payment Status

\* Webhook



\### Database



```

payment\_db

```



\---



\## 4.6 Notification Service



Responsible for user notifications.



\### Owns



\* Notifications



\### Consumes Events



\* ORDER\_CREATED

\* ORDER\_ASSIGNED

\* PAYMENT\_SUCCESS

\* ORDER\_DELIVERED

\* COMPLAINT\_RESOLVED



\### Database



```

notification\_db

```



\---



\# 5. Database Per Service



Each microservice owns exactly one database.



```text

Auth Service

&#x20;     │

&#x20;     ▼

&#x20; auth\_db



Order Service

&#x20;     │

&#x20;     ▼

&#x20;order\_db



Payment Service

&#x20;     │

&#x20;     ▼

payment\_db



Notification Service

&#x20;     │

&#x20;     ▼

notification\_db

```



\### Rule



\*\*No service is allowed to directly access another service's database.\*\*



Communication must occur through REST APIs or RabbitMQ events.



\---



\# 6. Communication Pattern



\## Synchronous Communication (REST)



Used when an immediate response is required.



Examples:



\* Frontend → Auth Service

\* Frontend → Order Service

\* Frontend → Payment Service



```text

Client

&#x20;  │

&#x20;  ▼

Gateway

&#x20;  │

&#x20;  ▼

Target Service

```



\---



\## Asynchronous Communication (RabbitMQ)



Used for background processing and notifications.



Examples:



\* Order Created

\* Order Assigned

\* Payment Successful

\* Order Delivered



```text

Order Service

&#x20;     │

&#x20;Publish Event

&#x20;     │

&#x20;     ▼

&#x20;RabbitMQ

&#x20;     │

&#x20;     ▼

Notification Service

```



\---



\# 7. Authentication Flow



```text

User



&#x20;  │



POST /login



&#x20;  │



&#x20;  ▼



Auth Service



&#x20;  │



Generate JWT + Refresh Token



&#x20;  │



&#x20;  ▼



Client Stores Token



&#x20;  │



&#x20;  ▼



API Gateway



&#x20;  │



Validate JWT



&#x20;  │



&#x20;  ▼



Forward Request



&#x20;  │



&#x20;  ▼



Target Microservice

```



\---



\# 8. Order Processing Flow



```text

Customer



&#x20;    │



Create Order



&#x20;    │



&#x20;    ▼



Order Service



&#x20;    │



Store Order



&#x20;    │



Publish ORDER\_CREATED



&#x20;    │



&#x20;    ▼



RabbitMQ



&#x20;    │



&#x20;    ▼



Notification Service



&#x20;    │



Store Notification

```



\---



\# 9. Payment Flow



```text

Customer



&#x20;     │



Create Payment



&#x20;     │



&#x20;     ▼



Payment Service



&#x20;     │



Cashfree API



&#x20;     │



Payment Link



&#x20;     │



Customer Pays



&#x20;     │



Cashfree Webhook



&#x20;     │



Update Payment



&#x20;     │



Publish PAYMENT\_SUCCESS



&#x20;     │



&#x20;     ▼



RabbitMQ



&#x20;     │



&#x20;     ▼



Notification Service

```



\---



\# 10. Deployment Architecture



```text

AWS EC2



│



├── Docker Engine



├── Docker Compose



│



├── Eureka Server



├── API Gateway



├── Auth Service



├── Order Service



├── Payment Service



├── Notification Service



├── RabbitMQ



├── MySQL (Auth)



├── MySQL (Order)



├── MySQL (Payment)



└── MySQL (Notification)

```



\---



\# 11. Technology Stack



| Layer             | Technology                  |

| ----------------- | --------------------------- |

| Backend           | Spring Boot 3.x             |

| Language          | Java 21                     |

| Frontend          | React                       |

| Build Tool        | Maven                       |

| Security          | Spring Security + JWT       |

| Service Discovery | Eureka                      |

| Gateway           | Spring Cloud Gateway        |

| Messaging         | RabbitMQ                    |

| Database          | MySQL                       |

| ORM               | Spring Data JPA / Hibernate |

| Containerization  | Docker                      |

| Orchestration     | Docker Compose              |

| Deployment        | AWS EC2                     |

| API Documentation | Swagger / OpenAPI           |



\---



\# 12. Design Principles



The system follows these architectural principles:



\* Single Responsibility Principle (SRP)

\* Database per Service

\* Loose Coupling

\* High Cohesion

\* Event-Driven Communication

\* Stateless Services

\* Layered Architecture

\* RESTful API Design

\* Secure by Default



\---



\# 13. Advantages of This Architecture



\* Independent deployment of services

\* Better scalability

\* Fault isolation

\* Easier maintenance

\* Technology flexibility

\* Production-ready design

\* Supports asynchronous processing

\* Suitable for cloud deployment



\---



~~# 14. Future Improvements~~



~~\* Config Server~~

~~\* API Rate Limiting~~

~~\* Circuit Breaker (Resilience4j)~~

~~\* Distributed Tracing (Zipkin)~~

~~\* Centralized Logging (ELK Stack)~~

~~\* Monitoring with Prometheus \& Grafana~~

~~\* Redis Caching~~

~~\* Kubernetes Deployment~~

~~\* CI/CD using GitHub Actions~~

~~\* Service Mesh (Istio)~~



~~---~~



~~# 15. Interview Talking Points~~



~~During interviews, be prepared to explain:~~



~~\* Why Microservices over Monolith?~~

~~\* Why Database per Service?~~

~~\* Why Eureka Server?~~

~~\* Why Spring Cloud Gateway?~~

~~\* Why RabbitMQ?~~

~~\* Why JWT instead of Sessions?~~

~~\* Why Docker?~~

~~\* Why REST + Event-Driven Communication?~~

~~\* How services communicate securely?~~

~~\* How failures are isolated in a distributed system?~~

~~\* How the system can be scaled horizontally?~~



