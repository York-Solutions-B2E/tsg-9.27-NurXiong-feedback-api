# tsg-9.27-NurXiong-feedback-api
Rest Api responsible for handling feedback events persisting them to postgres db
and publishing to kafka topic


## Features

- Validates request (service layer).
- Persists Feedback to Postgres (repo layer).
- Publishes a compact event to Kafka topic feedback-submitted.

## Tech Stack

**Server:** Springboot 3.9

**Build System** Maven

**Containerization** Docker

**Message Broker** Kafka

**Database** Postgres



## Prerequesites

- Jdk 21

- Docker desktop

- Ports 8080, 9092, 5432

- **local postgres be shutdown as to not conflict**


## API Reference

#### Post feedback entry

```http
  POST /api/v1/feedback
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `feedbackrequest` | `json` | **Required**. feedback to be published |

#### Get single feedback by id

```http
  GET /api/v1/feedback/${id}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `string` | **Required**. Id of feedback to fetch |

### Get list of feedbacks for member
```http
 GET /api/v1/feedback?memberId=<id>
 ```
 | Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `memberId`      | `string` | **Required**. memberId to fetch all feedback|

### Get simple health
```http
 GET /api/v1/health
 ```
 | Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `None`      | `None` | Get simple health of server|



## Installation

Clone repo 

```bash
git clone https://github.com/York-Solutions-B2E/tsg-9.27-NurXiong-feedback-api.git
cd feedback-api
```

Start Docker containers for DB & Kafka
```bash
Docker compose up -d
  ```

Clean compile the project
```bash
 mvn clean compile
 ```

Start spring-boot app
```bash
mvn spring-boot:run
```

Run all tests in project
```bash
mvn test
```







## Running Tests

To run tests, run the following command

```bash
  mvn test
```


## Example feedback request

```javascript
{
    "memberId": "m-01",
    "providerName": "Dr. Spring",
    "rating": 5,
    "comment": "Foe to friend. 5 stars"
}
```


## Authors

- [@AbdiNYork](https://github.com/AbdiNYork)
- [@leng03](https://www.github.com/leng03)
