\## 06-security.md



\---



\# 06 – Security Design (Microservices JWT System)



This document defines the complete security architecture for the Logistics Microservices System. The goal is to ensure \*\*authentication, authorization, and secure service-to-service communication\*\* using JWT and Spring Security.



\---



\# 1. Security Goals



The system must ensure:



\* Only authenticated users can access protected APIs

\* Each request is authorized based on role

\* Services do not trust requests without validation

\* No service depends on Auth Service at runtime for authentication

\* Stateless authentication using JWT

\* Secure communication between Gateway and services



\---



\# 2. Authentication Strategy



We use:



\* JWT (Access Token)

\* Refresh Token

\* Spring Security



\---



\## 2.1 Login Flow



```text

Client → Auth Service → JWT + Refresh Token → Client

```



\### Step-by-step:



1\. User logs in with email + password

2\. Auth Service validates credentials

3\. If valid:



&#x20;  \* Generate Access Token (JWT)

&#x20;  \* Generate Refresh Token

4\. Return both tokens to client



\---



\## 2.2 JWT Structure



```json

{

&#x20; "sub": "userId",

&#x20; "email": "user@example.com",

&#x20; "role": "CUSTOMER",

&#x20; "iat": 1710000000,

&#x20; "exp": 1710003600

}

```



\---



\## 2.3 Token Types



\### Access Token (JWT)



\* Short-lived (15–60 min)

\* Sent with every request

\* Used for authentication



\### Refresh Token



\* Long-lived (days/weeks)

\* Stored in DB

\* Used only to generate new access tokens



\---



\# 3. Request Flow (Security Perspective)



\## 3.1 Normal Request Flow



```text

Client

&#x20; ↓

API Gateway

&#x20; ↓ (JWT Validation)

Microservice (Order / Payment / etc.)

```



\---



\## 3.2 What Gateway Does



API Gateway is the \*\*first security layer\*\*.



It performs:



\* JWT extraction

\* JWT validation

\* Role check (optional pre-filtering)

\* Request forwarding



If token is invalid → request is rejected immediately.



\---



\# 4. Spring Security Architecture



Each microservice includes:



\* Spring Security Filter Chain

\* JWT Filter

\* Authentication Manager

\* UserDetailsService (only in Auth Service)



\---



\## 4.1 Security Filter Flow



```text

Request →

JWT Filter →

Validate Token →

Set Authentication Context →

Controller →

Response

```



\---



\## 4.2 Security Context



After validation:



```java

SecurityContextHolder.getContext().setAuthentication(auth);

```



This allows:



\* `@PreAuthorize`

\* Role checks

\* User identity access



\---



\# 5. Authorization Model



\## Roles in System



```text

CUSTOMER

DELIVERY\_AGENT

ADMIN

```



\---



\## Role Access Rules



| API Type      | CUSTOMER | AGENT | ADMIN |

| ------------- | -------- | ----- | ----- |

| Create Order  | ✔        | ❌     | ❌     |

| View Orders   | ✔        | ✔     | ✔     |

| Assign Agent  | ❌        | ❌     | ✔     |

| Update Status | ❌        | ✔     | ❌     |

| Manage Users  | ❌        | ❌     | ✔     |



\---



\## Method-Level Security Example



```java

@PreAuthorize("hasRole('ADMIN')")

public void assignAgent() {}

```



\---



\# 6. Endpoint Security Classification



\## 6.1 Public APIs (No Token Required)



\* `/api/v1/auth/register`

\* `/api/v1/auth/login`

\* `/api/v1/auth/refresh`



\---



\## 6.2 Protected APIs



All others require JWT:



\* `/api/v1/orders/\*\*`

\* `/api/v1/payments/\*\*`

\* `/api/v1/complaints/\*\*`

\* `/api/v1/notifications/\*\*`



\---



\# 7. JWT Validation Flow



\## Step-by-step inside services:



1\. Extract token from header:



```http

Authorization: Bearer <token>

```



2\. Validate signature using secret key



3\. Check expiration



4\. Extract claims:



&#x20;  \* userId

&#x20;  \* role



5\. Set authentication context



\---



\# 8. Refresh Token Flow



\## When Access Token expires:



```text

Client → /auth/refresh → New Access Token

```



\---



\## Refresh Token Rules:



\* Stored in database

~~\* One user can have multiple refresh tokens (optional)~~

\* Can be revoked on logout

\* Has expiry date



\---



\## Logout Flow:



```text

Client → Auth Service → Delete Refresh Token

```



Access token naturally expires.



\---



\# 9. Service-to-Service Security



Microservices do NOT call Auth Service for every request.



Instead:



\## Trusted Model:



\* Gateway validates JWT

\* Internal services trust Gateway



~~Optional (advanced):~~



~~\* Services can re-validate JWT locally~~



\---



\## Internal APIs Security



Internal APIs are:



```text

/internal/\*\*

```



These are protected using:



\* Service-level authentication

\* Network-level restriction (Docker network / VPC)

\* Optional shared secret header



Example:



```http

X-Internal-Token: secret-key

```



\---



\# 10. Password Security



\## Password Storage



Never store plain passwords.



Use:



```java

BCryptPasswordEncoder

```



\---



\## Flow:



```text

User password → BCrypt hash → Store in DB

```



Login:



```text

Input password → Compare with hash

```



\---



\# 11. Security in API Gateway



Gateway responsibilities:



\* Validate JWT

\* Reject invalid tokens

\* Forward valid requests

\* Add user headers to downstream services



Example forwarded headers:



```http

X-User-Id: 123

X-User-Role: CUSTOMER

```



\---



~~# 12. Common Security Mistakes (Avoid)~~



~~❌ Calling Auth Service on every request~~

~~❌ Storing JWT in database~~

~~❌ No refresh token strategy~~

~~❌ No role validation~~

~~❌ Exposing internal APIs publicly~~

~~❌ No token expiration handling~~



~~---~~



~~# 13. Security Best Practices~~



~~✔ Use short-lived access tokens~~

~~✔ Store refresh tokens securely~~

~~✔ Use HTTPS in production~~

~~✔ Validate JWT at gateway level~~

~~✔ Use role-based authorization~~

~~✔ Centralize security config~~

~~✔ Log authentication failures~~



~~---~~



\# 14. Threat Model (Simple)



| Threat              | Protection                      |

| ------------------- | ------------------------------- |

| Token theft         | Short expiry + refresh rotation |

| Unauthorized access | JWT validation                  |

| Role misuse         | RBAC                            |

| Replay attacks      | Expiry + signature              |

| Internal API abuse  | Network isolation               |



\---



\# 15. Summary Flow



```text

1\. User logs in

2\. Auth Service issues JWT

3\. Client sends JWT to Gateway

4\. Gateway validates JWT

5\. Request forwarded to service

6\. Service executes business logic

7\. Response returned

```



\---



