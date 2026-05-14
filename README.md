
# Tenant Service 🚀

## Overview
El **Tenant Service** es un microservicio construido con **Java Spring Boot** que gestiona tenants (inquilinos), planes de suscripción y suscripciones en una arquitectura multi-tenant. Sigue los principios de **Domain-Driven Design (DDD)** y está basado en el ejemplo de **CodelyTV Java DDD** ([repositorio](https://github.com/CodelyTV/java-ddd-example)).

Este servicio permite la gestión completa del ciclo de vida de tenants, incluyendo la creación de planes de suscripción con diferentes precios, la suscripción de tenants a planes, verificación de dominios personalizados, y gestión de pagos y renovaciones automáticas.

## 📌 Características Principales
- **Arquitectura Domain-Driven Design (DDD)** con separación clara de capas
- **Patrón CQRS** con separación de Commands y Queries
- **API GraphQL** para gestión de tenants, planes y suscripciones
- **Arquitectura orientada a eventos** usando RabbitMQ
- **Persistencia con MariaDB** y **Hibernate ORM**
- **Arquitectura Hexagonal** para mejor modularidad y testabilidad
- **Gestión de suscripciones** con soporte para períodos de prueba, renovación automática y cupones
- **Verificación de dominios personalizados** para tenants

## 📂 Estructura del Proyecto

El proyecto está organizado siguiendo los principios de DDD y Arquitectura Hexagonal:

```
src/main/com/jaapec/tenant/
├── plans/                          # Módulo de Planes de Suscripción
│   ├── application/                # Casos de uso (Commands & Queries)
│   │   ├── create/                 # Crear plan
│   │   ├── update/                 # Actualizar plan
│   │   ├── delete/                 # Eliminar plan
│   │   ├── find/                   # Buscar plan por ID
│   │   ├── search/                 # Buscar planes con filtros
│   │   ├── add_price/              # Agregar precio a plan
│   │   └── change_visibility/      # Cambiar visibilidad del plan
│   ├── domain/                     # Lógica de negocio
│   │   ├── Plan.java               # Agregado raíz
│   │   ├── PlanPrice.java          # Entidad de precio
│   │   ├── PlanRepository.java     # Interfaz del repositorio
│   │   ├── events/                 # Eventos de dominio
│   │   └── value_objects/          # Value Objects
│   └── infrastructure/             # Adaptadores
│       ├── controller/graphql/     # Controladores GraphQL
│       └── persistence/            # Repositorio MariaDB
│
├── tenant/                         # Módulo de Tenants
│   ├── application/                # Casos de uso
│   │   ├── create/                 # Crear tenant
│   │   ├── update/                 # Actualizar tenant
│   │   ├── find/                   # Buscar tenant
│   │   ├── search/                 # Buscar tenants con filtros
│   │   ├── change_domain/          # Cambiar dominio personalizado
│   │   ├── subscribe_to_plan/      # Suscribir tenant a plan
│   │   ├── activate_subscription/  # Activar suscripción
│   │   └── cancel_auto_renew/      # Cancelar renovación automática
│   ├── domain/                     # Lógica de negocio
│   │   ├── Tenant.java             # Agregado raíz
│   │   ├── TenantRepository.java   # Interfaz del repositorio
│   │   └── events/                 # Eventos de dominio
│   └── infrastructure/             # Adaptadores
│       ├── controller/graphql/     # Controladores GraphQL
│       └── persistence/            # Repositorio MariaDB
│
├── subscription/                   # Módulo de Suscripciones
│   ├── domain/                     # Lógica de negocio
│   │   └── TenantPlanSubscription.java  # Entidad de suscripción
│   └── infrastructure/             # Adaptadores
│
└── shared/                         # Código compartido
    ├── domain/                     # Abstracciones de dominio
    │   ├── bus/                    # Command, Query y Event Bus
    │   ├── criteria/               # Sistema de filtros y búsqueda
    │   └── ValueObjects/           # Value Objects base
    └── infrastructure/             # Infraestructura compartida
        ├── bus/                    # Implementaciones de buses
        ├── persistence/            # Configuración Hibernate
        └── controller/graphql/     # Configuración GraphQL
```

## 🛠 Tecnologías Utilizadas
- **Java 25** con toolchain
- **Spring Boot 4.0.6**
- **Netflix DGS GraphQL 12.0.0** (GraphQL para Spring Boot)
- **MariaDB 10.7** con **Hibernate 7.3.4**
- **RabbitMQ 3** (comunicación orientada a eventos)
- **Docker & Docker Compose** (despliegue containerizado)
- **Gradle 8** (gestión de dependencias y build)
- **JUnit 5** y **Mockito** (testing)

## ⚙️ Configuración

### 1️⃣ Prerequisitos
Asegúrate de tener instalado:
- **Java 25+**
- **Gradle 9+**
- **Docker & Docker Compose** (para MariaDB y RabbitMQ)

### 2️⃣ Variables de Entorno
Crea un archivo `.env` con las siguientes variables:

```env
# DATABASE MARIADB
DATABASE_HOST=localhost
DATABASE_PORT=3306
DATABASE_NAME=jaapec_tenant_service
DATABASE_USER=dbuser
DATABASE_PASSWORD=dbpasss

# RabbitMQ
RABBITMQ_HOST=localhost
RABBITMQ_PORT=5672
RABBITMQ_LOGIN=kcrqwise
RABBITMQ_PASSWORD=I3SCFxjoQT8zExp7vrx6k5Xr9uOaGx6a
RABBITMQ_EXCHANGE=domain_events
RABBITMQ_MAX_RETRIES=5
RABBITMQ_VHOST=kcrqwise
```

### 3️⃣ Ejecutar la Aplicación

#### Iniciar la base de datos y RabbitMQ
```sh
docker-compose up -d
```

#### Compilar y Ejecutar
```sh
./gradlew clean build
./gradlew bootRun
```

#### Generar JAR ejecutable
```sh
./gradlew bootJar
java -jar build/libs/tenant-service-0.0.1.jar
```

## 🔗 API GraphQL

El servicio expone una API GraphQL con las siguientes operaciones:

### 📋 Planes (Plans)

#### Mutations
```graphql
# Crear un nuevo plan
createPlan(request: RequestPlan!): Boolean!

# Actualizar un plan existente
updatePlan(id: ID!, request: RequestPlan!): Boolean!

# Agregar precio a un plan
addPlanPrice(id: ID!, request: RequestPlanPrice!): Boolean!

# Eliminar un plan
deletePlan(id: String!): Boolean!

# Cambiar visibilidad del plan (PUBLIC/PRIVATE)
changeVisibilityPlan(id: String!, request: PlanVisibility!): Boolean!
```

#### Queries
```graphql
# Buscar un plan por ID
findPlan(id: String!): PlanResponse!

# Buscar planes con filtros y paginación
searchPlans(
    filters: [FilterInput!]
    orderBy: String
    orderType: OrderType
    limit: Int
    offset: Int
): PaginatedPlanResponse!
```

#### Tipos
```graphql
type PlanResponse {
    id: String!
    name: String!
    description: String!
    prices: [PriceResponse]!
    maxUsers: Int!
    maxRoles: Int!
    maxAccounts: Int!
    maxInvoices: Int!
    status: PlanStatus!
    visibility: PlanVisibility!
    trialDays: Int!
    createdAt: String!
    updatedAt: String!
}

type PriceResponse {
    id: String!
    billingInterval: BillingInterval!
    amount: Int!
    currency: Currency!
    createdAt: String!
    updatedAt: String!
}
```

### 🏢 Tenants

#### Mutations
```graphql
# Crear un nuevo tenant
createTenant(request: RequestTenant!): Boolean!

# Actualizar un tenant
updateTenant(id: ID!, request: RequestTenant!): Boolean!

# Cambiar dominio personalizado
changeDomain(id: ID!, request: RequestDomainInput!): Boolean!

# Suscribir tenant a un plan
addSubscription(id: ID!, request: RequestSubscription!): Boolean!

# Cancelar renovación automática
cancelAutoRenew(tenantId: ID!, subscriptionId: ID!): Boolean!
```

#### Queries
```graphql
# Buscar un tenant por ID
findTenant(id: String!): TenantResponse!

# Buscar tenants con filtros y paginación
searchTenants(
    filters: [FilterInput!]
    orderBy: String
    orderType: OrderType
    limit: Int
    offset: Int
): PaginatedTenantResponse!

# Verificar estado de dominio personalizado
checkDomainVerification(id: String!): DomainVerificationResponse!
```

#### Tipos
```graphql
type TenantResponse {
    id: String!
    name: String!
    status: String!
    domain: String
    ownerId: String!
    createdAt: String!
    updatedAt: String!
}
```

### 🔍 Sistema de Filtros

El servicio incluye un sistema avanzado de filtros para búsquedas:

```graphql
input FilterInput {
    field: String!
    operator: Operator!
    value: String!
}

enum Operator {
    EQUAL
    NOT_EQUAL
    GT
    LT
    CONTAINS
    NOT_CONTAINS
}

enum OrderType {
    ASC
    DESC
    NONE
}
```

### Ejemplo de uso:
```graphql
query {
  searchPlans(
    filters: [
      { field: "status", operator: EQUAL, value: "ACTIVE" }
      { field: "visibility", operator: EQUAL, value: "PUBLIC" }
    ]
    orderBy: "createdAt"
    orderType: DESC
    limit: 10
    offset: 0
  ) {
    data {
      id
      name
      description
      prices {
        billingInterval
        amount
        currency
      }
    }
    pagination {
      currentPage
      totalPages
      totalItems
      hasNext
      hasPrevious
    }
  }
}
```

## 🏗️ Arquitectura

### Patrón CQRS
El servicio separa las operaciones de lectura (Queries) y escritura (Commands):
- **Commands**: Modifican el estado del sistema y publican eventos de dominio
- **Queries**: Solo leen datos sin efectos secundarios

### Event-Driven Architecture
Los cambios en el dominio publican eventos a RabbitMQ:
- `PlanCreatedDomainEvent`
- `PlanUpdatedDomainEvent`
- `TenantCreatedDomainEvent`
- `TenantSubscribeToPlanEvent`
- `TenantSubscriptionAutoRenewCanceledEvent`

### Value Objects
El dominio utiliza Value Objects para garantizar invariantes:
- `PlanId`, `PlanName`, `PlanDescription`
- `TenantId`, `TenantName`, `TenantDomain`
- `Amount`, `Currency`, `BillingInterval`

## 🧪 Testing

Ejecutar todos los tests:
```sh
./gradlew test
```

Generar reporte de cobertura:
```sh
./gradlew jacocoTestReport
```

El proyecto incluye:
- Tests unitarios con Mockito
- Tests de integración con base de datos
- Tests de controladores GraphQL
- Tests de repositorios Hibernate

## 🐳 Docker

### Construir imagen
```sh
docker build -t tenant-service:latest .
```

### Ejecutar con Docker Compose
```sh
docker-compose up -d
```

Esto levantará:
- MariaDB en puerto 3306
- RabbitMQ en puerto 5672 (Management UI en 15672)
- Tenant Service

## 📊 Monitoreo

El servicio incluye Spring Boot Actuator para monitoreo:
- Health check: `/actuator/health`
- Métricas: `/actuator/metrics`

## 🛠 Contribuir

1. Fork el repositorio
2. Crea una rama para tu feature (`git checkout -b feature/nueva-funcionalidad`)
3. Commit tus cambios (`git commit -m 'Agrega nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Abre un Pull Request

## 📝 Convenciones de Código

El proyecto utiliza:
- **Spotless** para formateo automático de código
- **SonarQube** para análisis de calidad de código
- **JaCoCo** para cobertura de tests

Formatear código:
```sh
./gradlew spotlessApply
```

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo [LICENSE](LICENSE) para más detalles.

---
🚀 **Construido con ❤️ usando Java & Spring Boot** 🚀 por [Eduardo Guastay](https://eduedu.dev)
