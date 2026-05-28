# Tenant Service

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=eduedudev_java-ddd-skeleton&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=eduedudev_java-ddd-skeleton)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=eduedudev_java-ddd-skeleton&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=eduedudev_java-ddd-skeleton)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=eduedudev_java-ddd-skeleton&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=eduedudev_java-ddd-skeleton)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=eduedudev_java-ddd-skeleton&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=eduedudev_java-ddd-skeleton)

Multi-tenant microservice built with **Java Spring Boot** for managing tenants, subscription plans, and subscriptions. Implements **Domain-Driven Design (DDD)**, **CQRS**, **Hexagonal Architecture**, and **Event-Driven Architecture**.

Based on the [CodelyTV Java DDD Example](https://github.com/CodelyTV/java-ddd-example).

## Tech Stack

| Category | Technology |
|----------|-----------|
| Language | Java 25 |
| Framework | Spring Boot 4.0.6 |
| API | Netflix DGS GraphQL 12.0.0 |
| Database | MariaDB + Hibernate 7.3.4 |
| Messaging | RabbitMQ (event-driven) |
| Build | Gradle 9.5 |
| Quality | Spotless, SonarQube, JaCoCo |
| Testing | JUnit 5, Mockito, DataFaker |
| Containerization | Docker & Docker Compose |

## Architecture

The project follows **Hexagonal Architecture** with clear separation of concerns:

- **Domain Layer** — Aggregates, Value Objects, Domain Events, Repository interfaces
- **Application Layer** — Use cases separated into Commands (write) and Queries (read) following CQRS
- **Infrastructure Layer** — Persistence (Hibernate/MariaDB), Controllers (GraphQL), Event Bus (RabbitMQ)

### Bounded Contexts

| Module | Responsibility |
|--------|---------------|
| `plans` | Subscription plan lifecycle: creation, pricing, visibility, deletion |
| `tenant` | Tenant management: creation, domain verification, subscription binding |
| `subscription` | Subscription entity: status, payments, auto-renewal, billing intervals |
| `shared` | Cross-cutting: buses (Command, Query, Event), criteria system, persistence, validation |

### Key Patterns

- **CQRS** — Commands modify state and emit domain events; Queries are side-effect free
- **Domain Events** — Published to RabbitMQ with MariaDB fallback and circuit breaker resilience
- **Criteria Pattern** — Reusable filtering, ordering, and pagination across all search operations
- **Value Objects** — Enforce domain invariants at construction time (`Amount`, `BillingInterval`, `Currency`, `TenantDomain`, etc.)

## Getting Started

### Prerequisites

- Java 25+
- Gradle 9+
- Docker & Docker Compose

### Environment Variables

Create a `.env` file:

```env
DATABASE_HOST=localhost
DATABASE_PORT=3306
DATABASE_NAME=jaapec_tenant_service
DATABASE_USER=dbuser
DATABASE_PASSWORD=dbpasss

RABBITMQ_HOST=localhost
RABBITMQ_PORT=5672
RABBITMQ_LOGIN=guest
RABBITMQ_PASSWORD=guest
RABBITMQ_EXCHANGE=domain_events
RABBITMQ_MAX_RETRIES=5
RABBITMQ_VHOST=/
```

### Run

```sh
docker-compose up -d          # Start MariaDB & RabbitMQ
./gradlew clean build         # Build
./gradlew bootRun             # Run
```

## GraphQL API

### Plans

| Operation | Type | Description |
|-----------|------|-------------|
| `createPlan` | Mutation | Create a subscription plan |
| `updatePlan` | Mutation | Update plan details |
| `addPlanPrice` | Mutation | Add a pricing option (interval + amount + currency) |
| `deletePlan` | Mutation | Remove a plan |
| `changeVisibilityPlan` | Mutation | Toggle PUBLIC/PRIVATE visibility |
| `findPlan` | Query | Get plan by ID |
| `searchPlans` | Query | Filter, order, and paginate plans |

### Tenants

| Operation | Type | Description |
|-----------|------|-------------|
| `createTenant` | Mutation | Register a new tenant |
| `updateTenant` | Mutation | Update tenant info |
| `changeDomain` | Mutation | Set custom domain |
| `addSubscription` | Mutation | Subscribe tenant to a plan |
| `cancelAutoRenew` | Mutation | Cancel automatic renewal |
| `findTenant` | Query | Get tenant by ID |
| `searchTenants` | Query | Filter, order, and paginate tenants |
| `checkDomainVerification` | Query | Verify custom domain DNS (CNAME lookup) |

### Filtering & Pagination

All search queries support a criteria system:

```graphql
query {
  searchPlans(
    filters: [{ field: "status", operator: EQUAL, value: "ACTIVE" }]
    orderBy: "createdAt"
    orderType: DESC
    limit: 10
    offset: 0
  ) {
    data { id name prices { amount currency billingInterval } }
    pagination { currentPage totalPages totalItems hasNext hasPrevious }
  }
}
```

**Operators:** `EQUAL`, `NOT_EQUAL`, `GT`, `LT`, `CONTAINS`, `NOT_CONTAINS`

## Domain Events

Events published to RabbitMQ on state changes:

- `PlanCreatedDomainEvent` / `PlanUpdatedDomainEvent` / `PlanDeletedDomainEvent`
- `ChangeVisibilityPlanDomainEvent`
- `TenantCreatedDomainEvent` / `TenantUpdatedDomainEvent`
- `TenantDomainChangedEvent` / `TenantChangedStatusDomainEvent`
- `TenantSubscribeToPlanEvent`
- `TenantSubscriptionAutoRenewCanceledEvent`

Resilience: Circuit breaker pattern with automatic fallback to MariaDB-based event store when RabbitMQ is unavailable.

## Testing

```sh
./gradlew test                # Run all tests
./gradlew jacocoTestReport    # Generate coverage report
```

Test coverage includes:
- Unit tests (domain logic, use cases) with Mockito
- Integration tests (Hibernate repositories, RabbitMQ event bus)
- Feature tests (GraphQL mutations & queries end-to-end)
- Object Mothers for test data generation

## Docker

```sh
docker build -t tenant-service:latest .
docker-compose up -d
```

## Monitoring

Spring Boot Actuator endpoints:
- `GET /actuator/health`
- `GET /actuator/metrics`

## Contributing

1. Fork the repo
2. Create a feature branch (`git checkout -b feature/my-feature`)
3. Commit and push
4. Open a Pull Request

Format code before committing:

```sh
./gradlew spotlessApply
```

## License

MIT — see [LICENSE](LICENSE) for details.

---

Built by [Eduardo Guastay](https://eduedu.dev)
