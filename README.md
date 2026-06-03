# Yoda Project

Yoda is a microservices-based application demonstrating a distributed system for account and user information management. 
It uses Camunda BPM for orchestration, Kafka for messaging, and Spring Boot for service implementation.


## Architecture

The project consists of the following modules:

- **Gateway**: The entry point for the system, providing a REST API and orchestrating requests via Kafka.
- **BPE (Business Process Engine)**: Uses Camunda BPM to manage business processes (e.g., retrieving user accounts).
- **Worker UserInfo**: An adapter service that provides user information.
- **Worker Payment**: An adapter service that provides account balance information.
- **Common**: Shared data models and utilities used across all services.

## Prerequisites

- Java 11 or higher
- Docker and Docker Compose
- Gradle (optional, using `./gradlew` is recommended)

## Getting Started

### 1. Infrastructure Setup

Start the required infrastructure (Kafka, Zookeeper, Kafdrop) using Docker Compose:

```bash
docker-compose -f _doc/docker-compose.yml up -d kafka kafdrop
```

### 2. Build the Project

Build all modules using the Gradle wrapper:

```bash
./gradlew build
```

### 3. Run the Applications

You can run each service individually using Gradle:

```bash
./gradlew :gateway:bootRun
./gradlew :bpe:bootRun
./gradlew :worker-userinfo:bootRun
./gradlew :worker-payment:bootRun
```

Alternatively, you can build Docker images and run them using Docker Compose (requires updating the `docker-compose.yml` with built images).

## API Documentation

The Gateway service provides Swagger UI for API exploration. Once the gateway is running, you can access it at:
`http://localhost:8081/swagger-ui.html`

### Key Endpoints

- **Get Accounts**: `GET /api/account`
- **Get User Info**: `GET /api/userInfo`
- **Charge Balance**: `POST /api/charge`
- **Withdraw Balance**: `POST /api/withdraw`

*Note: All requests require `X-TOKEN` and `X-OPERATION-ID` headers.*

## Infrastructure Monitoring

- **Kafdrop (Kafka Web UI)**: Access at `http://localhost:9000` to monitor Kafka topics and messages.