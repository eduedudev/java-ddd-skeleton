---
name: ddd-architecture-overview
description: Visión completa de la arquitectura DDD + Hexagonal + CQRS + Event-Driven del tenant-service. Úsala como primer contexto antes de cualquier tarea de diseño o cuando necesites entender cómo encajan las capas.
---

# DDD Architecture Overview — `tenant-service`

## Capas (de adentro hacia afuera)

```
┌─────────────────────────────────────────────────────────────┐
│  infrastructure/                                            │
│  ├── controller/graphql/   (DGS DataFetchers, schema)       │
│  ├── persistence/hibernate/ (MariaDB<X>Repository, .orm.xml)│
│  ├── bus/{command,query,event}/ (InMemory*, RabbitMq*, ...) │
│  └── spring/, validation/, circuit_breaker/                 │
│         │                                                   │
│         ▼ depende de                                        │
│  application/                                               │
│  ├── <use_case>/                                            │
│  │   ├── XxxCommand / XxxQuery   (record, primitivos)       │
│  │   ├── XxxCommandHandler / XxxQueryHandler  (@Service)    │
│  │   └── XxxCreator/Updater/Deleter/Finder/Searcher (@Service)│
│  └── XxxResponse  (DTO record)                              │
│         │                                                   │
│         ▼ depende de                                        │
│  domain/                                                    │
│  ├── <Aggregate>.java     (final class, AggregateRoot)      │
│  ├── <Aggregate>Repository.java  (interface, puerto)        │
│  ├── value_objects/   (final class por VO)                  │
│  ├── events/   (extends DomainEvent)                        │
│  └── exceptions   (extends DomainError)                     │
└─────────────────────────────────────────────────────────────┘
```

**Regla fundamental**: las flechas de dependencia solo apuntan hacia adentro. `domain` no importa nada de `application` ni de `infrastructure`. `application` no importa nada de `infrastructure`.

## Bounded Contexts

| Contexto | Carpeta | Aggregate Roots |
|---|---|---|
| Plans | `plans/` | `Plan` (con entity `PlanPrice`) |
| Tenants | `tenant/` | `Tenant` |
| Subscriptions | `subscription/` | `TenantPlanSubscription` (entity dependiente de `Tenant`) |
| Shared | `shared/` | — (cross-cutting) |
| Template | `template/` | — (esqueleto vacío para clonar) |

Los bounded contexts pueden referirse a clases de otros contextos solo cuando hay una relación clara (ej. `Tenant` referencia `Plan` al suscribirse).

## CQRS

- **Command side**: muta estado, devuelve `void`, **siempre** publica eventos al final.
- **Query side**: lee estado, devuelve `Response`, **nunca** muta nada ni publica eventos.

Buses:
- `CommandBus` → `InMemoryCommandBus` (resuelve por `Reflections` y dispatch por `ApplicationContext`).
- `QueryBus` → `InMemoryQueryBus`.
- `EventBus` → `RabbitMqEventBus` (`@Primary`) con failover a `MariaDBEventBus` mediante `SimpleCircuitBreaker(3 fallos, 30s, 1 prueba)`.

## Flujo end-to-end de un Command

```
GraphQL mutation (DataFetcher)
        │ valida con Validator (rules tipo Laravel)
        │ construye XxxCommand (primitivos)
        ▼
ApiController.dispatch(command) → CommandBus
        ▼
InMemoryCommandBus → busca handler por tipo en CommandHandlersInformation
        ▼
XxxCommandHandler.handle(command)
        │ mapea primitivos → Value Objects
        ▼
<Verbo><Aggregate> (caso de uso, @Service)
        │ 1. Repository.find(...) si carga agregado
        │ 2. agregado = agregado.<accion>(...)  (devuelve nueva instancia)
        │ 3. Repository.save / update / delete
        │ 4. eventBus.publish(agregado.pullDomainEvents())
        ▼
RabbitMqEventBus → publisher.publish(event, "domain_events")
        │ si falla 3 veces → CircuitBreaker abierto → fallback
        ▼
MariaDBEventBus.publish → INSERT en tabla domain_events
```

## Flujo end-to-end de una Query

```
GraphQL query (DataFetcher)
        │ construye XxxQuery
        ▼
ApiController.ask(query) → QueryBus
        ▼
InMemoryQueryBus → busca handler en QueryHandlersInformation
        ▼
XxxQueryHandler.handle(query)
        │ mapea primitivos → VOs / Filters / Order / Pagination
        ▼
<Aggregate>Finder.find(id) → Optional<XxxResponse>
o <Aggregate>Searcher.search(filters, order, pagination) → PaginatedResponse<XxxResponse>
        │ Repository.matching(criteria) → List<Aggregate>
        │ map a XxxResponse.fromAggregate(...)
        ▼
DataFetcher devuelve Response → DGS lo serializa
```

## Eventos de dominio

- Se registran dentro del agregado con `record(event)`.
- Se publican siempre desde el caso de uso con `eventBus.publish(aggregate.pullDomainEvents())`.
- `DomainEvent` aporta `aggregateId`, `eventId` (UUID), `occurredOn` (timestamp).
- Cada evento define `eventName()` (ej. `"plan.created"`), `toPrimitives()` (Map<String, Serializable>) y `fromPrimitives(...)` para deserialización.

## Persistencia

- **Hibernate XML mapping**, no JPA annotations.
- `HibernateRepository<T>` aporta `persist`, `merge`, `remove`, `byId`, `byCriteria`, `countByCriteria`, `isFieldValueUnique`.
- Cada aggregate tiene su `<Aggregate>.orm.xml` al lado del repositorio MariaDB.
- Composite-id: el id es un VO con `<key-property column="id" name="value" .../>`.
- Los VOs simples se mapean como `<component>` con `<property name="value" ... />`.

## API GraphQL

- Schemas modulares: cada bounded context define `infrastructure/controller/graphql/schema/schema.graphqls` que extiende `Mutation` y `Query` del schema compartido en `shared/infrastructure/controller/graphql/schema/schema.graphqls`.
- `CustomDGSConfiguration` carga todos los `.graphqls` por convención (ruta `/infrastructure/controller/graphql/schema/`).
- `GraphQLExceptionResolver` mapea `DomainError` a errores GraphQL con `extensions: {code, reason, value}`.

## Errores

| HTTP-like code | Clase | Cuándo |
|---|---|---|
| `E404` | `ResourceNotExist` | Recurso no existe |
| `E409` | `ResourceAlreadyExists` | Conflicto de unicidad |
| `E423` | `InvalidDomainException` | Dominio DNS inválido |
| Custom | `<X>Exception extends DomainError` | Reglas específicas de un BC |

Toda excepción de dominio lleva `errorCode`, `Message(messageKey, args)` (i18n vía `messages.properties`), `reason` y `value`.

## Cuándo usar qué

| Quiero... | Dónde va |
|---|---|
| Validar formato de un campo de entrada (longitud, regex, enum) | `Validator` rules en el DataFetcher |
| Validar invariante de negocio (no se puede tener dos suscripciones activas) | Constructor del VO o método del agregado |
| Cargar un agregado y operarlo | Caso de uso en `application/<accion>/<Verbo><Aggregate>.java` |
| Listar/filtrar/paginar | Query + `Searcher` + `Criteria` |
| Reaccionar a un evento de otro contexto | Crear `DomainEventSubscriber` (ver `shared/infrastructure/bus/event/`) |
| Persistir un campo nuevo | Editar `<Aggregate>.orm.xml` y agregar el VO al constructor del agregado |

## Referencias en el código

- Aggregate completo: `src/main/com/jaapec/tenant/plans/domain/Plan.java`.
- Slice completo de command: `plans/application/create/`.
- Slice completo de query: `plans/application/find/` y `plans/application/search/`.
- Repositorio + ORM XML: `plans/infrastructure/persistence/hibernate/`.
- Caso complejo (loops, validación, eventos múltiples): `tenant/domain/Tenant.java`.
- Event bus con failover: `shared/infrastructure/bus/event/rabbitmq/RabbitMqEventBus.java`.
