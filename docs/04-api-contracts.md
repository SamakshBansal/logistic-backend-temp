\# 04 - API Contracts



\# Logistics Management System



\*\*API Style:\*\* RESTful APIs



\*\*Version:\*\* v1



\*\*Authentication:\*\* JWT Bearer Token



\*\*Content-Type:\*\* `application/json`



\*\*Base URL\*\*



```text

/api/v1

```



\---



\# 1. API Standards



\## HTTP Methods



| Method | Purpose            |

| ------ | ------------------ |

| GET    | Retrieve Resources |

| POST   | Create Resource    |

| PUT    | Replace Resource   |

| PATCH  | Partial Update     |

| DELETE | Delete Resource    |



\---



\## Standard Response Format



\### Success Response



```json

{

&#x20;   "success": true,

&#x20;   "message": "Operation completed successfully.",

&#x20;   "data": {},

&#x20;   "timestamp": "2026-06-28T10:30:00Z"

}

```



\---



\### Error Response



```json

{

&#x20;   "success": false,

&#x20;   "errorCode": "ORDER\_NOT\_FOUND",

&#x20;   "message": "Order not found.",

&#x20;   "details": \[],

&#x20;   "timestamp": "2026-06-28T10:30:00Z"

}

```



\---



\## HTTP Status Codes



| Code | Meaning               |

| ---- | --------------------- |

| 200  | OK                    |

| 201  | Created               |

| 204  | No Content            |

| 400  | Bad Request           |

| 401  | Unauthorized          |

| 403  | Forbidden             |

| 404  | Not Found             |

| 409  | Conflict              |

| 500  | Internal Server Error |



\---



**# 2. Authentication APIs**



Base URL



```text

/api/v1/auth

```



\---



\## Register User



\### Endpoint



```http

POST /api/v1/auth/register

```



\### Request



```json

{

&#x20;   "fullName":"John Doe",

&#x20;   "email":"john@gmail.com",

&#x20;   "password":"Password@123",

&#x20;   "phoneNumber":"9876543210",

&#x20;   "role":"CUSTOMER"

}

```



\### Response



```json

{

&#x20;   "success":true,

&#x20;   "message":"User registered successfully."

}

```



\---



\## Login



```http

POST /api/v1/auth/login

```



\### Request



```json

{

&#x20;   "email":"john@gmail.com",

&#x20;   "password":"Password@123"

}

```



\### Response



```json

{

&#x20;   "accessToken":"jwt-token",

&#x20;   "refreshToken":"refresh-token",

&#x20;   "expiresIn":3600

}

```



\---



\## Refresh Token



```http

POST /api/v1/auth/refresh

```



\### Request



```json

{

&#x20;   "refreshToken":"xxxxx"

}

```



\### Response



```json

{

&#x20;   "accessToken":"new-jwt-token",

&#x20;   "expiresIn":3600

}

```



\---



\## Logout



```http

POST /api/v1/auth/logout

```



\---



\## Get Current User



```http

GET /api/v1/auth/me

```



\---



\## Change Password



```http

PUT /api/v1/auth/change-password

```



\---



**# 3. User APIs (Admin)**



Base URL



```text

/api/v1/users

```



\---



\## Get All Users



```http

GET /api/v1/users?page=0\&size=10

```



\---



\## Get User By ID



```http

GET /api/v1/users/{userId}

```



\---



\## Update User



```http

PUT /api/v1/users/{userId}

```



\---



\## Change User Role



```http

PATCH /api/v1/users/{userId}/role

```



Request



```json

{

&#x20;   "role":"DELIVERY\_AGENT"

}

```



\---



\## Delete User



```http

DELETE /api/v1/users/{userId}

```



\---



**# 4. Order APIs**



Base URL



```text

/api/v1/orders

```



\---



\## Create Order



```http

POST /api/v1/orders

```



\### Request



```json

{

&#x20;   "pickupAddress":"Pune",

&#x20;   "deliveryAddress":"Mumbai",

&#x20;   "receiverName":"Rahul Sharma",

&#x20;   "receiverPhone":"9876543210",

&#x20;   "packageType":"DOCUMENT",

&#x20;   "packageWeight":2.5

}

```



\### Response



```json

{

&#x20;   "trackingNumber":"TRK123456789",

&#x20;   "orderStatus":"CREATED"

}

```



\---



\## Get My Orders



```http

GET /api/v1/orders/my?page=0\&size=10

```



\---



\## Get Order Details



```http

GET /api/v1/orders/{orderId}

```



\---



\## Track Order



```http

GET /api/v1/orders/{orderId}/tracking

```



Response



```json

{

&#x20;   "trackingNumber":"TRK123456789",

&#x20;   "status":"IN\_TRANSIT",

&#x20;   "updatedAt":"2026-06-28T12:00:00Z"

}

```



\---



\## Cancel Order



```http

PATCH /api/v1/orders/{orderId}/cancel

```



\---



\## Get All Orders (Admin)



```http

GET /api/v1/orders

```



Supported Query Parameters



```text

status

customerId

agentId

page

size

sort

```



Example



```http

GET /api/v1/orders?status=CREATED\&page=0\&size=10

```



\---



\## Assign Delivery Agent



```http

PATCH /api/v1/orders/{orderId}/assign-agent

```



Request



```json

{

&#x20;   "agentId":"UUID"

}

```



\---



\## Update Order Status



```http

PATCH /api/v1/orders/{orderId}/status

```



Request



```json

{

&#x20;   "status":"PICKED\_UP"

}

```



\---



\## Get Assigned Orders



```http

GET /api/v1/orders/assigned

```



\---



**# 5. Complaint APIs**



Base URL



```text

/api/v1/complaints

```



\---



\## Create Complaint



```http

POST /api/v1/complaints

```



Request



```json

{

&#x20;   "orderId":"UUID",

&#x20;   "subject":"Package Damaged",

&#x20;   "description":"The package was damaged during delivery."

}

```



\---



\## Get My Complaints



```http

GET /api/v1/complaints/my

```



\---



\## Get Complaint



```http

GET /api/v1/complaints/{complaintId}

```



\---



\## Get All Complaints (Admin)



```http

GET /api/v1/complaints

```



\---



\## Resolve Complaint



```http

PATCH /api/v1/complaints/{complaintId}/resolve

```



Request



```json

{

&#x20;   "resolution":"Refund Approved"

}

```



\---



**# 6. Payment APIs**



Base URL



```text

/api/v1/payments

```



\---



\## Create Payment



```http

POST /api/v1/payments

```



Request



```json

{

&#x20;   "orderId":"UUID"

}

```



Response



```json

{

&#x20;   "paymentLink":"https://payments.cashfree.com/..."

}

```



\---



\## Payment Status



```http

GET /api/v1/payments/{orderId}

```



\---



\## Payment History



```http

GET /api/v1/payments/history?page=0\&size=10

```



\---



\## Cashfree Webhook



```http

POST /api/v1/payments/webhook

```



\*\*Called by Cashfree.\*\*



No authentication required.



Webhook Signature must be verified.



\---



**# 7. Notification APIs**



Base URL



```text

/api/v1/notifications

```



\---



\## Get Notifications



```http

GET /api/v1/notifications?page=0\&size=20

```



\---



\## Mark Notification Read



```http

PATCH /api/v1/notifications/{notificationId}/read

```



\---



\## Mark All Read



```http

PATCH /api/v1/notifications/read-all

```



\---



**# 8. Internal APIs**



These endpoints are only used for service-to-service communication.



\---



\## Validate User



```http

GET /internal/users/{userId}

```



\---



\## Get User Details



```http

GET /internal/users/{userId}/basic

```



\---



\## Validate Delivery Agent



```http

GET /internal/agents/{agentId}

```



\---



\# 9. Authentication Requirements



| API           | Authentication |

| ------------- | -------------- |

| Register      | No             |

| Login         | No             |

| Refresh Token | No             |

| Logout        | Yes            |

| Profile       | Yes            |

| Orders        | Yes            |

| Payments      | Yes            |

| Complaints    | Yes            |

| Notifications | Yes            |



Authorization Header



```http

Authorization: Bearer <JWT\_TOKEN>

```



\---



\# 10. Role-Based Access Matrix



| API               | Customer | Delivery Agent | Admin |

| ----------------- | -------- | -------------- | ----- |

| Register          | ✅        | ❌              | ❌     |

| Login             | ✅        | ✅              | ✅     |

| View Profile      | ✅        | ✅              | ✅     |

| Create Order      | ✅        | ❌              | ❌     |

| My Orders         | ✅        | ❌              | ❌     |

| Track Order       | ✅        | ✅              | ✅     |

| Cancel Order      | ✅        | ❌              | ❌     |

| Assigned Orders   | ❌        | ✅              | ❌     |

| Update Status     | ❌        | ✅              | ❌     |

| Assign Agent      | ❌        | ❌              | ✅     |

| View All Orders   | ❌        | ❌              | ✅     |

| Raise Complaint   | ✅        | ❌              | ❌     |

| Resolve Complaint | ❌        | ❌              | ✅     |

| Payment           | ✅        | ❌              | ❌     |

| Notifications     | ✅        | ✅              | ✅     |

| Manage Users      | ❌        | ❌              | ✅     |



\---



\# 11. Pagination Standard



List endpoints support:



```text

?page=0

\&size=10

\&sort=createdAt,desc

```



Example



```http

GET /api/v1/orders?page=0\&size=20\&sort=createdAt,desc

```



\---



\# 12. Validation Rules



\## Register



\* Email must be valid.

\* Password must contain uppercase, lowercase, number, and special character.

\* Phone number must be valid.



\---



\## Create Order



\* Pickup address required.

\* Delivery address required.

\* Receiver name required.

\* Receiver phone required.

\* Package weight must be greater than zero.



\---



\## Payment



\* Order must exist.

\* Payment cannot be created twice for the same order.



\---



\## Complaint



\* Order must belong to the logged-in customer.

\* Description cannot be empty.



\---



\# 13. Error Codes



| Error Code                | Description                     |

| ------------------------- | ------------------------------- |

| USER\_NOT\_FOUND            | User does not exist             |

| EMAIL\_ALREADY\_EXISTS      | Email already registered        |

| INVALID\_CREDENTIALS       | Invalid email or password       |

| ACCESS\_DENIED             | Unauthorized access             |

| ORDER\_NOT\_FOUND           | Order does not exist            |

| ORDER\_ALREADY\_CANCELLED   | Order already cancelled         |

| INVALID\_ORDER\_STATUS      | Invalid order status transition |

| PAYMENT\_FAILED            | Payment processing failed       |

| PAYMENT\_ALREADY\_COMPLETED | Payment already completed       |

| COMPLAINT\_NOT\_FOUND       | Complaint does not exist        |

| INTERNAL\_SERVER\_ERROR     | Unexpected server error         |



\---



~~# 14. API Versioning Strategy~~



~~Current Version~~



~~```text~~

~~/api/v1~~

~~```~~



~~Future versions~~



~~```text~~

~~/api/v2~~

~~/api/v3~~

~~```~~



~~Versioning ensures backward compatibility while introducing new features.~~



~~---~~



~~# 15. OpenAPI Documentation~~



~~Each microservice exposes Swagger UI.~~



~~Example URLs:~~



~~```text~~

~~Auth Service~~

~~/swagger-ui/index.html~~



~~Order Service~~

~~/swagger-ui/index.html~~



~~Payment Service~~

~~/swagger-ui/index.html~~



~~Notification Service~~

~~/swagger-ui/index.html~~

~~```~~



~~---~~



~~# 16. Best Practices~~



~~\* RESTful endpoint naming~~

~~\* Consistent response format~~

~~\* Proper HTTP status codes~~

~~\* Pagination for list APIs~~

~~\* Validation using Bean Validation~~

~~\* Global exception handling~~

~~\* JWT-secured endpoints~~

~~\* API versioning~~

~~\* Idempotent webhook processing~~

~~\* OpenAPI/Swagger documentation~~



~~---~~



~~# 17. Interview Talking Points~~



~~Be prepared to explain:~~



~~\* Why REST over RPC?~~

~~\* Why version APIs?~~

~~\* Why use JWT Bearer tokens?~~

~~\* Why standardize API responses?~~

~~\* Why use proper HTTP methods?~~

~~\* Why paginate list endpoints?~~

~~\* How is webhook security implemented?~~

~~\* How does role-based authorization work?~~

~~\* How do internal service APIs differ from public APIs?~~

~~\* How would you maintain backward compatibility when releasing a new API version?~~



