\# 03 - Database Design



\# Logistics Management System



\*\*Architecture:\*\* Database per Service



\*\*Database:\*\* MySQL 8.x



\*\*ORM:\*\* Spring Data JPA (Hibernate)



\*\*Primary Key Strategy:\*\* UUID



\---



\# 1. Database Design Overview



The Logistics Management System follows the \*\*Database per Service\*\* pattern.



Each microservice owns its own database and is solely responsible for reading and writing its data.



No microservice is allowed to directly access another service's database.



Communication between services happens through:



\* REST APIs

\* RabbitMQ Events



\---



\# 2. Database Architecture



```text

&#x20;                   Logistics Management System



&#x20;               +-------------------------------+

&#x20;               |      Auth Service             |

&#x20;               +---------------+---------------+

&#x20;                               |

&#x20;                               ▼

&#x20;                          auth\_db





&#x20;               +-------------------------------+

&#x20;               |      Order Service            |

&#x20;               +---------------+---------------+

&#x20;                               |

&#x20;                               ▼

&#x20;                          order\_db





&#x20;               +-------------------------------+

&#x20;               |     Payment Service           |

&#x20;               +---------------+---------------+

&#x20;                               |

&#x20;                               ▼

&#x20;                         payment\_db





&#x20;               +-------------------------------+

&#x20;               |  Notification Service         |

&#x20;               +---------------+---------------+

&#x20;                               |

&#x20;                               ▼

&#x20;                     notification\_db

```



\---



\# 3. Naming Conventions



\## Tables



\* snake\_case

\* plural names



Example



```text

users

orders

payments

notifications

complaints

refresh\_tokens

```



\---



\## Columns



```text

created\_at



updated\_at



customer\_id



order\_status



payment\_status

```



\---



\## Primary Keys



Every table uses UUID.



Example



```sql

id CHAR(36) PRIMARY KEY

```



\---



\## Audit Fields



Every entity should contain:



```sql

created\_at



updated\_at

```



\---



\# 4. auth\_db



The Auth Service is responsible for user authentication and authorization.



\---



\## users



| Column         | Type         | Description                       |

| -------------- | ------------ | --------------------------------- |

| id             | CHAR(36)     | UUID                              |

| full\_name      | VARCHAR(100) | User Full Name                    |

| email          | VARCHAR(255) | Unique Email                      |

| password       | VARCHAR(255) | BCrypt Password                   |

| phone\_number   | VARCHAR(20)  | Contact Number                    |

| role           | ENUM         | CUSTOMER / DELIVERY\_AGENT / ADMIN |

| account\_status | ENUM         | ACTIVE / BLOCKED                  |

| email\_verified | BOOLEAN      | Email Verification                |

| created\_at     | DATETIME     | Created Time                      |

| updated\_at     | DATETIME     | Updated Time                      |



\### Constraints



\* Email must be unique.

\* Password stored encrypted.

\* Role mandatory.



\---



\## refresh\_tokens



| Column      | Type         |

| ----------- | ------------ |

| id          | CHAR(36)     |

| user\_id     | CHAR(36)     |

| token       | VARCHAR(500) |

| expiry\_date | DATETIME     |

| revoked     | BOOLEAN      |

| created\_at  | DATETIME     |



\---



\## ER Diagram



```text

users

\------

id (PK)



&#x20;       1

&#x20;       │

&#x20;       │

&#x20;       │

&#x20;       ▼

refresh\_tokens

\---------------

id (PK)

user\_id (FK)

```



\---



\# 5. order\_db



Responsible for logistics operations.



\---



\## orders



| Column                 | Type          |

| ---------------------- | ------------- |

| id                     | CHAR(36)      |

| tracking\_number        | VARCHAR(30)   |

| customer\_id            | CHAR(36)      |

| pickup\_address         | TEXT          |

| delivery\_address       | TEXT          |

| receiver\_name          | VARCHAR(100)  |

| receiver\_phone         | VARCHAR(20)   |

| package\_type           | ENUM          |

| package\_weight         | DECIMAL(10,2) |

| payment\_status         | ENUM          |

| order\_status           | ENUM          |

| assigned\_agent\_id      | CHAR(36)      |

| expected\_delivery\_date | DATE          |

| created\_at             | DATETIME      |

| updated\_at             | DATETIME      |



\---



\## complaints



| Column           | Type         |

| ---------------- | ------------ |

| id               | CHAR(36)     |

| order\_id         | CHAR(36)     |

| customer\_id      | CHAR(36)     |

| subject          | VARCHAR(150) |

| description      | TEXT         |

| complaint\_status | ENUM         |

| resolution       | TEXT         |

| resolved\_by      | CHAR(36)     |

| resolved\_at      | DATETIME     |

| created\_at       | DATETIME     |



\---



\## Order Status Enum



```text

CREATED



PAYMENT\_PENDING



PAYMENT\_COMPLETED



ASSIGNED



PICKED\_UP



IN\_TRANSIT



DELIVERED



CANCELLED

```



\---



\## Complaint Status Enum



```text

OPEN



IN\_PROGRESS



RESOLVED



CLOSED

```



\---



\## ER Diagram



```text

orders

\-------

id (PK)



&#x20;      1

&#x20;      │

&#x20;      │

&#x20;      ▼



complaints

\-----------

id (PK)

order\_id (FK)

```



\---



\# 6. payment\_db



Responsible for payment processing.



\---



\## payments



| Column              | Type          |

| ------------------- | ------------- |

| id                  | CHAR(36)      |

| order\_id            | CHAR(36)      |

| customer\_id         | CHAR(36)      |

| cashfree\_order\_id   | VARCHAR(100)  |

| cashfree\_payment\_id | VARCHAR(100)  |

| amount              | DECIMAL(10,2) |

| currency            | VARCHAR(10)   |

| payment\_method      | VARCHAR(50)   |

| payment\_status      | ENUM          |

| payment\_time        | DATETIME      |

| created\_at          | DATETIME      |

| updated\_at          | DATETIME      |



\---



\## Payment Status



```text

PENDING



SUCCESS



FAILED



CANCELLED



REFUNDED

```



\---



\## ER Diagram



```text

payments



id (PK)



order\_id



customer\_id

```



\---



\# 7. notification\_db



Responsible for notifications.



\---



\## notifications



| Column            | Type         |

| ----------------- | ------------ |

| id                | CHAR(36)     |

| user\_id           | CHAR(36)     |

| title             | VARCHAR(100) |

| message           | TEXT         |

| notification\_type | ENUM         |

| is\_read           | BOOLEAN      |

| created\_at        | DATETIME     |



\---



\## Notification Types



```text

ORDER



PAYMENT



COMPLAINT



SYSTEM

```



\---



\# 8. Database Relationships



Since this is a microservices architecture, there are \*\*no foreign key constraints across databases\*\*.



Instead, services store only the IDs of external entities.



Example:



```text

Order Service



customer\_id



assigned\_agent\_id

```



These IDs refer to users managed by the Auth Service.



\---



\# 9. Database Relationships Summary



\## Auth Service



```text

User



1



↓



Many



↓



Refresh Tokens

```



\---



\## Order Service



```text

Order



1



↓



Many



↓



Complaints

```



\---



\## Payment Service



```text

One Order



↓



One Payment

```



\---



\## Notification Service



```text

One User



↓



Many Notifications

```



\---



\# 10. Indexing Strategy



Indexes improve query performance.



\## users



```sql

INDEX(email)

```



\---



\## orders



```sql

INDEX(customer\_id)



INDEX(order\_status)



INDEX(assigned\_agent\_id)



INDEX(tracking\_number)

```



\---



\## complaints



```sql

INDEX(order\_id)



INDEX(customer\_id)

```



\---



\## payments



```sql

INDEX(order\_id)



INDEX(customer\_id)



INDEX(payment\_status)

```



\---



\## notifications



```sql

INDEX(user\_id)



INDEX(is\_read)

```



\---



\# 11. Soft Delete Strategy



Instead of deleting important business data, records should be marked inactive.



Recommended columns:



```sql

is\_deleted BOOLEAN



deleted\_at DATETIME

```



Useful for:



\* Orders

\* Users

\* Complaints



\---



\# 12. Data Integrity Rules



\## User



\* Email unique

\* Password encrypted

\* Role mandatory



\---



\## Order



\* Tracking number unique

\* Customer required

\* Pickup address required

\* Delivery address required



\---



\## Complaint



\* Order must exist

\* Customer must own the order

\* Description required



\---



\## Payment



\* One payment per order

\* Amount cannot be negative

\* Payment status mandatory



\---



\## Notification



\* Must belong to one user

\* Message required



\---



\# 13. Entity Ownership



| Service              | Owns                  |

| -------------------- | --------------------- |

| Auth Service         | Users, Refresh Tokens |

| Order Service        | Orders, Complaints    |

| Payment Service      | Payments              |

| Notification Service | Notifications         |



No entity is shared between services.



\---



~~# 14. Future Database Enhancements~~



~~The schema can be extended with additional tables.~~



~~### Order History~~



~~```text~~

~~order\_history~~

~~```~~



~~Stores every status transition.~~



~~---~~



~~### Payment Audit~~



~~```text~~

~~payment\_audit~~

~~```~~



~~Stores webhook payloads.~~



~~---~~



~~### Notification Preferences~~



~~```text~~

~~notification\_preferences~~

~~```~~



~~Allows users to control notification channels.~~



~~---~~



~~### Delivery Tracking~~



~~```text~~

~~delivery\_tracking~~

~~```~~



~~Stores GPS coordinates and timestamps.~~



~~---~~



~~### Audit Logs~~



~~```text~~

~~audit\_logs~~

~~```~~



~~Tracks administrative actions.~~



~~---~~



~~# 15. Best Practices~~



~~\* Database per Service~~

~~\* UUID Primary Keys~~

~~\* Audit Columns~~

~~\* Indexed Search Columns~~

~~\* No Cross-Service Foreign Keys~~

~~\* Immutable Transaction Records~~

~~\* Soft Deletes for Important Data~~

~~\* Normalized Tables~~

~~\* Consistent Naming Conventions~~

~~\* Hibernate Entity Validation~~



~~---~~



~~# 16. Interview Talking Points~~



~~Be prepared to explain:~~



~~\* Why use \*\*Database per Service\*\*?~~

~~\* Why choose \*\*UUID\*\* over auto-increment IDs?~~

~~\* Why avoid cross-service foreign keys?~~

~~\* How do services reference entities in other databases?~~

~~\* Why are indexes important?~~

~~\* Why use soft deletes?~~

~~\* How does this design support scalability?~~

~~\* How would you evolve the schema without breaking other services?~~



