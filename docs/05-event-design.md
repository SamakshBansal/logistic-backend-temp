\# 05-event-design.md



\# Event Design (RabbitMQ Messaging Flow)



This document defines all asynchronous events used in the system.



These events are used for \*\*inter-service communication via RabbitMQ\*\*.



\---



\# 1. Why Event Design Matters



In microservices:



\- Services should NOT directly depend on each other

\- Communication should be asynchronous where possible

\- Events help achieve loose coupling



Instead of:



```



Order Service → Notification Service (direct call ❌)



```



We use:



```



Order Service → RabbitMQ → Notification Service



````



\---



\# 2. Event Broker



We use:



\- RabbitMQ (Message Broker)



Purpose:



\- Decouple services

\- Enable async communication

\- Improve scalability



\---



\# 3. Event Structure (Standard Format)



All events follow a common structure:



```json

{

&#x20; "eventId": "uuid",

&#x20; "eventType": "EVENT\_NAME",

&#x20; "timestamp": "2026-06-28T10:30:00Z",

&#x20; "source": "service-name",

&#x20; "data": {

&#x20;   // event-specific payload

&#x20; }

}

````



\---



\# 4. Events in the System



\## 4.1 ORDER\_CREATED



\### Producer



\* Order Service



\### Consumers



\* Notification Service



\### Triggered When



\* Customer places a new order



\### Payload



```json

{

&#x20; "eventType": "ORDER\_CREATED",

&#x20; "data": {

&#x20;   "orderId": "uuid",

&#x20;   "customerId": "uuid",

&#x20;   "pickupAddress": "...",

&#x20;   "deliveryAddress": "...",

&#x20;   "status": "CREATED"

&#x20; }

}

```



\---



\## 4.2 ORDER\_ASSIGNED



\### Producer



\* Order Service (Admin assigns delivery agent)



\### Consumers



\* Notification Service



\### Triggered When



\* Admin assigns delivery agent to an order



\### Payload



```json

{

&#x20; "eventType": "ORDER\_ASSIGNED",

&#x20; "data": {

&#x20;   "orderId": "uuid",

&#x20;   "agentId": "uuid",

&#x20;   "customerId": "uuid",

&#x20;   "status": "ASSIGNED"

&#x20; }

}

```



\---



\## 4.3 ORDER\_STATUS\_UPDATED



\### Producer



\* Order Service (Agent updates status)



\### Consumers



\* Notification Service



\### Triggered When



\* Order status changes (PICKED\_UP, IN\_TRANSIT, DELIVERED)



\### Payload



```json

{

&#x20; "eventType": "ORDER\_STATUS\_UPDATED",

&#x20; "data": {

&#x20;   "orderId": "uuid",

&#x20;   "customerId": "uuid",

&#x20;   "agentId": "uuid",

&#x20;   "oldStatus": "PICKED\_UP",

&#x20;   "newStatus": "IN\_TRANSIT"

&#x20; }

}

```



\---



\## 4.4 PAYMENT\_INITIATED



\### Producer



\* Payment Service



\### Consumers



\* Notification Service



\### Triggered When



\* Payment link is generated



\### Payload



```json

{

&#x20; "eventType": "PAYMENT\_INITIATED",

&#x20; "data": {

&#x20;   "paymentId": "uuid",

&#x20;   "orderId": "uuid",

&#x20;   "customerId": "uuid",

&#x20;   "amount": 250.00,

&#x20;   "status": "PENDING"

&#x20; }

}

```



\---



\## 4.5 PAYMENT\_SUCCESS



\### Producer



\* Payment Service (Webhook from Cashfree)



\### Consumers



\* Order Service (optional update)

\* Notification Service



\### Triggered When



\* Cashfree confirms successful payment



\### Payload



```json

{

&#x20; "eventType": "PAYMENT\_SUCCESS",

&#x20; "data": {

&#x20;   "paymentId": "uuid",

&#x20;   "orderId": "uuid",

&#x20;   "customerId": "uuid",

&#x20;   "amount": 250.00,

&#x20;   "paymentStatus": "SUCCESS"

&#x20; }

}

```



\---



\## 4.6 PAYMENT\_FAILED



\### Producer



\* Payment Service



\### Consumers



\* Notification Service



\### Triggered When



\* Payment fails or is declined



\### Payload



```json

{

&#x20; "eventType": "PAYMENT\_FAILED",

&#x20; "data": {

&#x20;   "paymentId": "uuid",

&#x20;   "orderId": "uuid",

&#x20;   "customerId": "uuid",

&#x20;   "reason": "INSUFFICIENT\_FUNDS"

&#x20; }

}

```



\---



\## 4.7 ORDER\_DELIVERED



\### Producer



\* Order Service



\### Consumers



\* Notification Service



\### Triggered When



\* Delivery agent marks order as DELIVERED



\### Payload



```json

{

&#x20; "eventType": "ORDER\_DELIVERED",

&#x20; "data": {

&#x20;   "orderId": "uuid",

&#x20;   "customerId": "uuid",

&#x20;   "agentId": "uuid",

&#x20;   "deliveredAt": "2026-06-28T10:30:00Z"

&#x20; }

}

```



\---



\# 5. Event Flow Diagram



```

Order Service

&#x20;   ├── ORDER\_CREATED

&#x20;   ├── ORDER\_ASSIGNED

&#x20;   ├── ORDER\_STATUS\_UPDATED

&#x20;   └── ORDER\_DELIVERED

&#x20;           ↓

&#x20;       RabbitMQ

&#x20;           ↓

Notification Service



Payment Service

&#x20;   ├── PAYMENT\_INITIATED

&#x20;   ├── PAYMENT\_SUCCESS

&#x20;   └── PAYMENT\_FAILED

&#x20;           ↓

&#x20;       RabbitMQ

&#x20;           ↓

Notification Service

```



\---



\# 6. Routing Strategy (RabbitMQ)



We use:



\### Exchange Type



\* topic exchange



\### Exchange Name



```

logistics.exchange

```



\### Routing Keys



```

order.created

order.assigned

order.status.updated

order.delivered



payment.initiated

payment.success

payment.failed

```



\---



\# 7. Queue Design



\## Notification Queue



```

notification.queue

```



Bindings:



```

order.\*

payment.\*

```



\---



~~# 8. Design Rules~~



~~### 1. Events are immutable~~



~~Never modify past events.~~



~~### 2. Events are source-driven~~



~~Only one service produces a specific event.~~



~~### 3. No direct service communication~~



~~No REST calls between services for core workflows.~~



~~### 4. Events should be minimal~~



~~Only include required data.~~



~~### 5. Always include metadata~~



~~\* eventType~~

~~\* timestamp~~

~~\* eventId~~



~~---~~



~~# 9. Interview Explanation Points~~



~~You should be able to explain:~~



~~### Why RabbitMQ?~~



~~\* Asynchronous communication~~

~~\* Decoupling services~~

~~\* Better scalability~~

~~\* Fault tolerance~~



~~---~~



~~### Why event-driven architecture?~~



~~\* Reduces service dependency~~

~~\* Improves system resilience~~

~~\* Enables real-time updates~~



~~---~~



~~### Why not direct API calls?~~



~~\* Tight coupling~~

~~\* Cascading failures~~

~~\* Hard to scale~~



~~---~~



~~# 10. Final Summary~~



~~This event design ensures:~~



~~\* Loose coupling between services~~

~~\* Scalable communication system~~

~~\* Clean separation of responsibilities~~

~~\* Interview-ready architecture~~



~~---~~



