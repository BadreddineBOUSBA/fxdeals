# ClusteredData Warehouse - FX Deals 

## Overview

the project is built with Spring Boot 3.2, PostgreSQL, and Docker.

## Features

- ✅ RESTful API for FX deal saving
- ✅ Comprehensive validation (JSR-303 + Custom validators : a simple validator utility with proper exception handling and a specific method for further logic)
- ✅ Duplicate deal prevention with unique constraints
- ✅ Structured logging with SLF4J
- ✅ Global exception handling with propper HTTP codes in return
- ✅ Unit and Integration tests for both the controller and the service with a good coverage but not high

## Tech Stack

- **Framework**: Spring Boot 3.2.0
- **Database**: PostgreSQL 15
- **Build Tool**: Maven 3.9
- **Java Version**: 17
- **Testing**: JUnit 5, Mockito


## API Documentation

### Base URL
```
http://localhost:8080/api/v1/deals
```

### Endpoints

#### 1. Create FX Deal

**POST** 

**Request Body:**
```json
{
  "dealUniqueId": "DEAL-2024-001",
  "fromCurrencyCode": "USD",
  "toCurrencyCode": "EUR",
  "dealTimestamp": "2024-11-25T10:30:00Z",
  "dealAmount": 1000000.50
}
```

**Success Response (201 Created):**
```json
{
  "id": 1,
  "dealUniqueId": "DEAL-2024-001",
  "fromCurrencyCode": "USD",
  "toCurrencyCode": "EUR",
  "dealTimestamp": "2024-11-25T10:30:00Z",
  "dealAmount": 1000000.50,
  "createdAt": "2024-11-25T10:30:05.123Z"
}
```

**Error Response (400 Bad Request):**
```json
{
  "timestamp": "2024-11-25T10:30:05.123Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "error": "fromCurrencyCode: Invalid ISO 4217 currency code"
  
}
```

**Error Response (409 Conflict):**
```json
{
  "timestamp": "2024-11-25T10:30:05.123Z",
  "status": 409,
  "error": "Conflict",
  "message": "Deal with ID 'DEAL-2024-001' already exists"
}
```
## Validation Rules

### Field Validations

| Field | Rules |
|-------|-------|
| dealUniqueId | Required, Not blank, Unique |
| fromCurrencyCode | Required, Valid ISO 4217 code (3 letters) |
| toCurrencyCode | Required, Valid ISO 4217 code (3 letters) |
| dealTimestamp | Required |
| dealAmount | Required, Positive |

### Currency Code Validation

The application validates against major & standard currency codes including:
- USD, EUR, GBP (Major currencies)


### Application Properties

Edit `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/fxdeals
    username: postgres
    password: postgres
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false

logging:
  level:
    com.bloomberg.warehouse: INFO
```

### Environment Variables

For Docker deployment, edit `docker-compose.yml`:

```yaml
environment:
  - SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/fxdeals
  - SPRING_DATASOURCE_USERNAME=postgres
  - SPRING_DATASOURCE_PASSWORD=postgres
```



## Logging

The application uses SLF4J with Logback for structured logging:

- **INFO**: Normal operations (deal creation)
- **WARN**: Validation failures, duplicate attempts
- **ERROR**: System errors, database issues


## Error Handling

The application implements comprehensive error handling:

### HTTP Status Codes

- **201 Created**: Deal successfully created
- **400 Bad Request**: Validation errors ( currency codes, deal amount not positive ...)
- **409 Conflict**: Duplicate deal ID
- **500 Internal Server Error**: System errors ( for possible technical errors)  &&  I hope there is none left !

### Error Response Format

```json
{
  "timestamp": "2024-11-25T10:30:05.123Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": "Field 'dealAmount': must be positive"
}
```

## Database Schema

### FX_DEALS Table

```sql
CREATE TABLE fx_deals (
    id BIGSERIAL PRIMARY KEY,
    deal_unique_id VARCHAR(100) NOT NULL UNIQUE,
    from_currency_code VARCHAR(3) NOT NULL,
    to_currency_code VARCHAR(3) NOT NULL,
    deal_timestamp TIMESTAMP NOT NULL,
    deal_amount NUMERIC(19, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_amount_positive CHECK (deal_amount > 0)
);

CREATE INDEX idx_deal_unique_id ON fx_deals(deal_unique_id);
CREATE INDEX idx_deal_timestamp ON fx_deals(deal_timestamp);
CREATE INDEX idx_currency_codes ON fx_deals(from_currency_code, to_currency_code);
```

## Development

### Local Development Setup

```bash
# Start PostgreSQL only
docker-compose up -d postgres

# Run application locally
mvn spring-boot:run

# Run with specific profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

---

**Built with ❤️ using Spring Boot, PostgreSQL, and Docker**
