# 📝 README: Feedback API (`tsg-9.27-{teamName}-feedback-api`)
 
This Spring Boot application serves as the core REST API for submitting and retrieving provider feedback. It implements a critical component of the architecture: **Request Validation** → **Postgres Persistence** → **Kafka Event Publishing**.
 
### 🚀 Architecture & Stack
 
* **Technology:** Spring Boot, Spring Data JPA, Kafka Producer
* **Language:** **Java 21** (Required for local execution)
* **Database:** PostgreSQL
* **Messaging:** Apache Kafka
 
The application enforces a clean, layered architecture: **thin controllers**, with all business logic (validation, DTO mapping, persistence, messaging) residing in the **Service Layer**.
 
---
 
### ⚙️ Local Development & Running
 
#### Prerequisites
 
To run the application and tests **locally** (outside of Docker), you must have:
 
* **Java 21 (JDK)**
* **Maven**
 
#### Dockerized Setup (Recommended)
 
The entire project stack is managed via a top-level `docker-compose.yaml` (located in a designated repository).
 
1.  **Build and Run All Services:**
    Execute the following command from the root directory containing the top-level `docker-compose.yaml`:
    ```bash
    docker compose up -d --build
    ```
    *This command builds the necessary images and starts all services in detached mode.*

    ```bash
    docker logs -f [container name]
    ```
    *This command shows live logs for any running container selected - etc. 'feedback_api'
 
2.  **Access Points (Local Docker Ports):**
 
| Service | Port | Description |
| :--- | :--- | :--- |
| **`feedback-api`** | `8080` | This REST API service |
| **Postgres** | `5432` | Database instance |
| **Kafka Broker** | `9092` | Messaging |
| **Kafka UI** | `8085` | Dashboard to view topics/events |
| **Frontend UI** | `5173` | React Application |
| **Consumer** | `8081` | Analytics Consumer |
 
---
 
### 💡 API Endpoints
 
Base URL: `/api/v1`
 
#### Endpoints Summary
 
| Method | Endpoint | Description | Status Codes |
| :--- | :--- | :--- | :--- |
| **`POST`** | `/feedback` | Submit new feedback (Validates, Persists, Publishes to Kafka). | **201** Created, **400** Bad Request |
| **`GET`** | `/feedback/{id}` | Retrieve feedback by its UUID. | **200** OK, **404** Not Found |
| **`GET`** | `/feedback?memberId=<id>` | Retrieve list of feedback for a specific member ID. | **200** OK (List may be empty) |
| **`GET`** | `/health` | Simple application health check. | **200** OK |
 
---
#### URLS
 
Feedback Server
http://localhost:8080/api/v1
 
Kafka UI
http://localhost:8085
 
Frontend-UI
http://localhost:5173
 
Swagger-UI
http://localhost:8080/swagger-ui/index.html
 
---
 
### 📝 Request & Response Details
 
#### 1. POST /feedback
 
**Validation Rules enforced by Service Layer:**
* `memberId`: required, 36 chars.
* `providerName`: required, 80 chars.
* `rating`: required, integer 1-5.
* `comment`: optional, 200 chars.
* Unknown fields in DTO are rejected.
 
**Request Body (JSON):**
```json
{
  "memberId": "m-123",
  "providerName": "Dr. Smith",
  "rating": 4,
  "comment": "Great experience."
}
```
#### Success Response (201 Created): Includes generated id and server-set submittedAt.
```json
{
  "id": "a1b2c3d4-e5f6-7890-abcd-ef0123456789",
  "memberId": "m-123",
  "providerName": "Dr. Smith",
  "rating": 4,
  "comment": "Great experience.",
  "submittedAt": "2025-11-19T18:28:10Z"
}
```
 
#### Error Response (400 Bad Request): Returns a machine-readable JSON array of errors.
```json
{
  "errors": [
    {
      "field": "comment",
      "message": "Must be ≤ 200 characters"
    },
    {
      "field": "rating",
      "message": "Rating must be between 1 and 5"
    }
  ]
}
```
 
## Running Tests
**Java 21 is required for all local testing.**
 
##### Option 1: Local Testing (Requires Java 21)
 
Compile and Run All Tests:
 
```bash
mvn clean compile
mvn test
```
####  This executes Unit tests (Service logic), MockMvc tests (Controller layer)
 
 
 
##### **Option 2: Dockerized Testing (Alternative)**
 
Use the provided shell script to execute tests within a consistent Docker container environment:
```bash
./tesh.sh
```
 
