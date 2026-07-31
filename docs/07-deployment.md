\# 📦 07-deployment.md — Microservices Deployment Guide



\---



\# 1. Deployment Strategy Overview



This project follows a \*\*containerized microservices deployment model\*\* using Docker and AWS EC2.



\### Key Principles



\* Each service runs independently

\* Each service is containerized (Docker)

\* All services communicate via API Gateway + Eureka

\* External dependencies (MySQL, RabbitMQ) also containerized

\* Single command deployment using Docker Compose



\---



\# 2. Final Production Architecture



```text

&#x20;                   ┌─────────────────────┐

&#x20;                   │   React Frontend          │

&#x20;                   └─────────┬───────────┘

&#x20;                                │

&#x20;                                ▼

&#x20;                ┌──────────────────────────┐

&#x20;                │     API Gateway                  │

&#x20;                │ Spring Cloud Gateway             │

&#x20;                └─────────┬────────────────┘

&#x20;                             │

&#x20;                             ▼

&#x20;             ┌─────────────────────────────┐

&#x20;             │      Eureka Server                   │

&#x20;             │ Service Discovery Registry           │

&#x20;             └─────────┬───────────────────┘

&#x20;                          │

────────────────────────────────────────────────────

&#x20;       MICROSERVICES LAYER

────────────────────────────────────────────────────



&#x20;  ┌──────────────┐  ┌──────────────┐

&#x20;  │ Auth Service     │  │ Order Service    │

&#x20;  └──────┬───────┘  └──────┬───────┘

&#x20;           │                      │

&#x20;  ┌──────▼───────┐  ┌──────▼────────┐

&#x20;  │ Payment Svc       │  │ Notification      │

&#x20;  └───────────────┘  └──────────────┘



────────────────────────────────────────────────────

&#x20;       INFRASTRUCTURE LAYER

────────────────────────────────────────────────────



&#x20;  ┌──────────────┐   ┌──────────────┐

&#x20;  │   MySQL DBs      │   │  RabbitMQ        │

&#x20;  └──────────────┘   └──────────────┘

```



\---



\# 3. Deployment Approach



We use:



\* Docker for containerization

\* Docker Compose for orchestration

\* AWS EC2 for hosting



\---



\# 4. Docker Strategy (Per Service)



Each service has:



```text

Dockerfile

```



\### Example structure:



```

auth-service/

&#x20;├── Dockerfile

&#x20;├── target/

&#x20;├── src/

```





\---



\# 5. Docker Images Breakdown



| Service              | Port | Description      |

| -------------------- | ---- | ---------------- |

| Eureka Server        | 8761 | Service registry |

| API Gateway          | 8080 | Entry point      |

| Auth Service         | 8081 | Authentication   |

| Order Service        | 8082 | Orders           |

| Payment Service      | 8083 | Payments         |

| Notification Service | 8084 | Notifications    |



\---



\# 6. Docker Compose (Core File)



\## docker-compose.yml



```yaml

version: "3.8"



services:



&#x20; eureka-server:

&#x20;   build: ./eureka-server

&#x20;   ports:

&#x20;     - "8761:8761"



&#x20; api-gateway:

&#x20;   build: ./api-gateway

&#x20;   ports:

&#x20;     - "8080:8080"

&#x20;   depends\_on:

&#x20;     - eureka-server



&#x20; auth-service:

&#x20;   build: ./auth-service

&#x20;   ports:

&#x20;     - "8081:8081"

&#x20;   depends\_on:

&#x20;     - mysql-auth

&#x20;     - eureka-server



&#x20; order-service:

&#x20;   build: ./order-service

&#x20;   ports:

&#x20;     - "8082:8082"

&#x20;   depends\_on:

&#x20;     - mysql-order

&#x20;     - rabbitmq



&#x20; payment-service:

&#x20;   build: ./payment-service

&#x20;   ports:

&#x20;     - "8083:8083"

&#x20;   depends\_on:

&#x20;     - mysql-payment



&#x20; notification-service:

&#x20;   build: ./notification-service

&#x20;   ports:

&#x20;     - "8084:8084"

&#x20;   depends\_on:

&#x20;     - rabbitmq



&#x20; mysql-auth:

&#x20;   image: mysql:8

&#x20;   environment:

&#x20;     MYSQL\_ROOT\_PASSWORD: root

&#x20;     MYSQL\_DATABASE: auth\_db

&#x20;   ports:

&#x20;     - "3307:3306"



&#x20; mysql-order:

&#x20;   image: mysql:8

&#x20;   environment:

&#x20;     MYSQL\_ROOT\_PASSWORD: root

&#x20;     MYSQL\_DATABASE: order\_db

&#x20;   ports:

&#x20;     - "3308:3306"



&#x20; mysql-payment:

&#x20;   image: mysql:8

&#x20;   environment:

&#x20;     MYSQL\_ROOT\_PASSWORD: root

&#x20;     MYSQL\_DATABASE: payment\_db

&#x20;   ports:

&#x20;     - "3309:3306"



&#x20; rabbitmq:

&#x20;   image: rabbitmq:3-management

&#x20;   ports:

&#x20;     - "5672:5672"

&#x20;     - "15672:15672"

```



\---



\# 7. Service Startup Order



Very important in microservices:



\### Step 1



Start infrastructure first:



\* MySQL

\* RabbitMQ



\### Step 2



\* Eureka Server



\### Step 3



\* API Gateway



\### Step 4



\* Microservices



\---



\# 8. Running the Project Locally



\## Step 1: Build all services



```bash

mvn clean package -DskipTests

```



\---



\## Step 2: Start everything



```bash

docker compose up --build

```



\---



\## Step 3: Verify



| Service     | URL                                              |

| ----------- | ------------------------------------------------ |

| Eureka      | \[http://localhost:8761](http://localhost:8761)   |

| Gateway     | \[http://localhost:8080](http://localhost:8080)   |

| RabbitMQ UI | \[http://localhost:15672](http://localhost:15672) |



\---



\# 9. Environment Variables Strategy



Never hardcode configs.



Example:



```yaml

spring:

&#x20; datasource:

&#x20;   url: jdbc:mysql://mysql-auth:3306/auth\_db

&#x20;   username: root

&#x20;   password: root

```



\---



\## JWT config



```yaml

jwt:

&#x20; secret: mysecretkey

&#x20; expiration: 3600000

```



\---



\# 10. AWS Deployment (Production Setup)



\## Step 1: Launch EC2



\* Ubuntu 22.04

\* t2.medium (recommended)



\---



\## Step 2: Install dependencies



```bash

sudo apt update

sudo apt install docker.io -y

sudo apt install docker-compose -y

sudo apt install git -y

```



\---



\## Step 3: Clone project



```bash

git clone <repo-url>

cd logistics-project

```



\---



\## Step 4: Run system



```bash

docker compose up -d

```



\---



\# 11. Security in Deployment



Open only required ports:



| Port  | Service                          |

| ----- | -------------------------------- |

| 80    | Gateway (optional reverse proxy) |

| 8080  | API Gateway                      |

| 8761  | Eureka (internal only ideally)   |

| 15672 | RabbitMQ dashboard (dev only)    |



\---



~~# 12. Production Improvements (IMPORTANT)~~



~~add:~~



~~---~~



~~## 1. Reverse Proxy (Nginx)~~



~~Instead of exposing gateway directly:~~



~~```~~

~~Nginx → API Gateway~~

~~```~~



~~---~~



~~## 2. HTTPS (SSL)~~



~~Use:~~



~~\* Let's Encrypt~~

~~\* Certbot~~



~~---~~



~~## 3. Centralized Logging~~



~~Add:~~



~~\* ELK stack OR~~

~~\* Spring Cloud Sleuth + Zipkin~~



~~---~~



~~## 4. Health Checks~~



~~Add actuator:~~



~~```yaml~~

~~/actuator/health~~

~~```~~



~~---~~



~~## 5. Retry + Circuit Breaker~~



~~Use:~~



~~\* Resilience4j~~



~~---~~



~~# 13. Deployment Architecture Summary (Interview Answer)~~



~~If asked:~~



~~> How did you deploy your system?~~



~~You can answer:~~



~~> I containerized all microservices using Docker, orchestrated them using Docker Compose, and deployed everything on an AWS EC2 instance. Each service runs independently and registers with Eureka for service discovery. API Gateway acts as a single entry point. MySQL and RabbitMQ are also containerized for consistency across environments.~~



\---





