\# 01 - Requirements Specification



\# Logistics Management System



\*\*Project Name:\*\* Logistics Management System



\*\*Architecture:\*\* Microservices



\*\*Backend:\*\* Spring Boot 3.x, Java 21



\*\*Frontend:\*\* React



\*\*Databases:\*\* MySQL (Database per Service)



\*\*Communication:\*\*



\* REST APIs

\* RabbitMQ (Asynchronous Events)



\*\*Authentication:\*\*



\* JWT Authentication

\* Refresh Tokens



\*\*Service Discovery:\*\*



\* Eureka Server



\*\*API Gateway:\*\*



\* Spring Cloud Gateway



\*\*Deployment:\*\*



\* Docker

\* Docker Compose

\* AWS EC2



\---



\# 1. Project Overview



The Logistics Management System is a courier and package delivery platform designed using a Microservices Architecture. The application enables customers to create shipment orders, make payments, track deliveries, raise complaints, and receive notifications. Delivery agents manage assigned deliveries, while administrators oversee users, orders, complaints, and delivery assignments.



The primary objective of this project is to demonstrate the implementation of a production-style distributed system using Spring Boot microservices and modern cloud-native technologies.



\---



\# 2. Objectives



The project aims to:



\* Learn Microservices Architecture

\* Implement secure authentication using JWT

\* Apply Role-Based Access Control (RBAC)

\* Integrate third-party payment services (Cashfree)

\* Implement asynchronous communication using RabbitMQ

\* Containerize services using Docker

\* Deploy the application on AWS EC2

\* Gain hands-on experience with distributed systems



\---



\# 3. Scope



The system provides functionality for three different types of users:



\* Customer

\* Delivery Agent

\* Administrator



Each user has different permissions and responsibilities.



\---



\# 4. User Roles



\## 4.1 Customer



Customers can:



\* Register

\* Login

\* Update Profile

\* Change Password

\* Create Orders

\* View Order History

\* Track Orders

\* Cancel Orders (before pickup)

\* Make Payments

\* View Payment History

\* Raise Complaints

\* View Complaint Status

\* View Notifications

\* Mark Notifications as Read



\---



\## 4.2 Delivery Agent



Delivery agents can:



\* Login

\* View Assigned Orders

\* View Order Details

\* Update Delivery Status

\* View Notifications



\---



\## 4.3 Administrator



Administrators can:



\* Manage Users

\* View All Orders

\* Assign Delivery Agents

\* Manage Complaints

\* Resolve Complaints

\* Monitor Payments

\* View System Statistics

\* View Notifications



\---



\# 5. Functional Requirements



\## Authentication Module



The system shall allow users to:



\* Register

\* Login

\* Logout

\* Refresh JWT Token

\* Change Password

\* View Profile



The system shall:



\* Encrypt passwords using BCrypt

\* Generate JWT Access Tokens

\* Generate Refresh Tokens

\* Validate tokens before accessing secured APIs



\---



\## User Management Module



Administrator can:



\* View all users

\* Search users

\* Update user information

\* Change user roles

\* Delete users



\---



\## Order Management Module



Customers can:



\* Create shipment orders

\* View order details

\* View all their orders

\* Track delivery progress

\* Cancel eligible orders



Administrators can:



\* View all orders

\* Filter orders

\* Assign delivery agents



Delivery Agents can:



\* View assigned orders

\* Update delivery status



\---



\## Complaint Management Module



Customers can:



\* Raise complaints

\* View complaint status



Administrators can:



\* View complaints

\* Resolve complaints



\---



\## Payment Module



Customers can:



\* Initiate payment

\* Complete payment through Cashfree

\* View payment status

\* View payment history



System shall:



\* Generate payment links

\* Verify payments using Cashfree Webhooks

\* Update payment status automatically



\---



\## Notification Module



The system shall notify users when:



\* Order created

\* Order assigned

\* Payment successful

\* Order delivered

\* Complaint resolved



Notifications shall be stored in the database.



\---



\# 6. Non-Functional Requirements



\## Performance



\* Average API response time below 500ms

\* Handle multiple concurrent requests

\* Efficient database queries



\---



\## Scalability



Each microservice should be independently scalable.



\---



\## Availability



Services should be independently deployable.



\---



\## Security



\* JWT Authentication

\* BCrypt Password Encoding

\* HTTPS Ready

\* Role-Based Authorization

\* Input Validation

\* SQL Injection Prevention



\---



\## Reliability



The system should continue operating even if one service is temporarily unavailable.



\---



\## Maintainability



\* Layered Architecture

\* Clean Code

\* DTO Pattern

\* Global Exception Handling

\* Logging

\* API Documentation



\---



\# 7. Service Boundaries



\## Auth Service



Responsible for:



\* Users

\* Roles

\* Authentication

\* JWT

\* Refresh Tokens



Does NOT manage:



\* Orders

\* Payments

\* Notifications



\---



\## Order Service



Responsible for:



\* Orders

\* Tracking

\* Complaints

\* Agent Assignment



Does NOT manage:



\* Authentication

\* Payments



\---



\## Payment Service



Responsible for:



\* Payments

\* Transactions

\* Cashfree Integration



Does NOT modify Orders directly.



\---



\## Notification Service



Responsible for:



\* Notifications

\* Event Consumption

\* Notification History



\---



\# 8. Business Rules



\## User Registration



\* Email must be unique.

\* Password must satisfy complexity rules.

\* User role is mandatory.

\* Passwords are never stored in plain text.



\---



\## Order Rules



\* Only authenticated customers can create orders.

\* Every order belongs to one customer.

\* Orders receive a unique tracking number.

\* Orders start in the `CREATED` state.

\* Orders cannot be modified after pickup.

\* Orders cannot be cancelled after pickup.



\---



\## Delivery Rules



\* One delivery agent may have multiple assigned orders.

\* One order can have only one assigned delivery agent.

\* Status updates follow a predefined workflow.



\---



\## Complaint Rules



\* Complaint must reference an existing order.

\* Only the customer who placed the order can create a complaint.

\* Closed complaints cannot be modified.



\---



\## Payment Rules



\* One payment belongs to one order.

\* Payment verification occurs via Cashfree Webhook.

\* Duplicate webhook events must be ignored (idempotency).

\* Payment status is updated only after successful verification.



\---



\# 9. Order Lifecycle



```text

CREATED

&#x20;   │

&#x20;   ▼

PAYMENT\_PENDING

&#x20;   │

&#x20;   ▼

PAYMENT\_COMPLETED

&#x20;   │

&#x20;   ▼

ASSIGNED

&#x20;   │

&#x20;   ▼

PICKED\_UP

&#x20;   │

&#x20;   ▼

IN\_TRANSIT

&#x20;   │

&#x20;   ▼

DELIVERED

```



\### Cancellation Rules



```text

CREATED

&#x20;  │

&#x20;  └──► CANCELLED



PAYMENT\_COMPLETED

&#x20;  │

&#x20;  └──► CANCELLED (Before Pickup)



PICKED\_UP

&#x20;  │

&#x20;  └──► Cancellation Not Allowed

```



\---



~~# 10. Assumptions~~



~~\* Internet connection is available.~~

~~\* Users possess valid email addresses.~~

~~\* Cashfree Sandbox credentials are configured.~~

~~\* RabbitMQ is available.~~

~~\* MySQL databases are accessible.~~

~~\* Docker is installed for local deployment.~~



~~---~~



~~# 11. Constraints~~



~~\* Database-per-Service architecture.~~

~~\* Services communicate via REST and RabbitMQ.~~

~~\* Services never access another service's database directly.~~

~~\* Authentication uses JWT only.~~

~~\* Payment integration uses Cashfree Sandbox.~~

~~\* Docker Compose is used for local orchestration.~~



~~---~~



~~# 12. Future Enhancements~~



~~Potential future improvements include:~~



~~\* OTP verification~~

~~\* Email notifications~~

~~\* SMS notifications~~

~~\* Real-time order tracking using WebSockets~~

~~\* GPS integration~~

~~\* Delivery route optimization~~

~~\* Admin analytics dashboard~~

~~\* Elasticsearch for advanced searching~~

~~\* Redis for caching~~

~~\* Kubernetes deployment~~

~~\* CI/CD pipeline with GitHub Actions~~

~~\* API Rate Limiting~~

~~\* Distributed Tracing (Zipkin)~~

~~\* Centralized Configuration Server~~

~~\* Prometheus \& Grafana Monitoring~~



~~---~~



~~# 13. Success Criteria~~



~~The project will be considered complete when:~~



~~\* All microservices communicate successfully.~~

~~\* Authentication is secure using JWT.~~

~~\* Orders can be created and tracked.~~

~~\* Payments are processed through Cashfree Sandbox.~~

~~\* Notifications are delivered using RabbitMQ.~~

~~\* All services run using Docker Compose.~~

~~\* The application is deployed successfully on AWS EC2.~~

~~\* API documentation is available through Swagger/OpenAPI.~~

~~\* The project can be explained confidently in technical interviews, including architecture decisions, service interactions, security model, and deployment strategy.~~



