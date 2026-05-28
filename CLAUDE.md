# CLAUDE.md — Guía del proyecto `tenant-service`

> Este archivo es el contrato de trabajo para cualquier agente (Claude, Kiro, Codex, etc.) que opere sobre este repositorio. Léelo completo antes de modificar código.
> Las skills detalladas viven en `.claude/skills/<nombre>/SKILL.md` y deben consultarse para cada tarea concreta.

---

## 1. Identidad del proyecto

Microservicio multi-tenant en **Java 25 + Spring Boot 4.0.6** que gestiona `tenants`, `plans` y `subscriptions`. Implementa:

- **Domain-Driven Design (DDD)** con bounded contexts.
- **Arquitectura Hexagonal** (domain ↔ application ↔ infrastructure).
- **CQRS** (commands de escritura + queries de lectura, separados).
- **Event-Driven Architecture** (eventos publicados a RabbitMQ con failover MariaDB y circuit breaker).
- **API GraphQL** con Netflix DGS 12.0.0.
- **Persistencia** con Hibernate 7.3.4 sobre MariaDB usando mappings XML (no JPA annotations).

Inspirado en el [CodelyTV Java DDD Example](https://github.com/CodelyTV/java-ddd-example).

---

## 2. Reglas de oro (no negociables)

1. **Nunca uses anotaciones JPA (`@Entity`, `@Id`, `@Column`)**. La persistencia se declara en archivos `*.orm.xml` dentro de `infrastructure/persistence/hibernate/`.
2. **Nunca uses `@Service`, `@Component`, `@Repository` de Spring** en clases de aplicación o dominio. Usa la anotación propia `com.jaapec.tenant.shared.domain.Service`. Esa anotación es la que el `@ComponentScan` del `Starter` filtra.
3. **El dominio no conoce Spring, Hibernate, GraphQL ni nada de infraestructura.** Solo Java puro y otras clases de `domain` o `shared.domain`.
4. **Los agregados son inmutables.** Cada operación de negocio devuelve una nueva instancia (`return new Plan(...)`), nunca muta `this`.
5. **Todos los campos primitivos del dominio se envuelven en Value Objects.** Nada de `String name` o `int amount` sueltos en agregados.
6. **Los Value Objects validan en el constructor.** Si el valor es inválido, lanzan una excepción que extiende `DomainError`.
7. **Los agregados registran eventos con `record(...)`** y los handlers de aplicación los publican con `eventBus.publish(aggregate.pullDomainEvents())`.
8. **CQRS estricto:**
   - Commands → mutan estado, devuelven `void`, emiten eventos.
   - Queries → no tienen efectos secundarios, devuelven `Response`.
9. **No introduzcas dependencias nuevas** sin avisar. La lista oficial está en `build.gradle`.
10. **Spotless es obligatorio.** Antes de proponer un commit, el código debe pasar `./gradlew spotlessApply`.

---

## 3. Estructura de carpetas

```
src/main/com/jaapec/tenant/
├── Starter.java                          # @SpringBootApplication
├── plans/                                # Bounded context: planes
│   ├── domain/
│   │   ├── Plan.java                     # AggregateRoot
│   │   ├── PlanPrice.java                # Entity (no AR)
│   │   ├── PlanRepository.java           # interface (puerto)
│   │   ├── value_objects/                # PlanId, PlanName, Amount, ...
│   │   └── events/                       # PlanCreatedDomainEvent, ...
│   ├── application/
│   │   ├── PlanResponse.java             # DTO de salida
│   │   ├── create/                       # CreatePlanCommand + Handler + PlanCreator
│   │   ├── update/
│   │   ├── delete/
│   │   ├── find/
│   │   ├── search/
│   │   ├── change_visibility/
│   │   └── add_price/
│   └── infrastructure/
│       ├── controller/
│       │   ├── RequestPlan.java          # input GraphQL
│       │   └── graphql/
│       │       ├── plan/                 # DataFetchers
│       │       └── schema/schema.graphqls
│       └── persistence/hibernate/
│           ├── MariaDBPlanRepository.java
│           ├── Plan.orm.xml
│           └── Prices.orm.xml
├── tenant/                               # Bounded context: tenants (misma estructura)
├── subscription/                         # Bounded context: suscripciones
├── shared/                               # Cross-cutting
│   ├── domain/
│   │   ├── AggregateRoot.java
│   │   ├── Service.java                  # @interface marcador
│   │   ├── DomainError.java
│   │   ├── ResourceNotExist.java         # E404
│   │   ├── ResourceAlreadyExists.java    # E409
│   │   ├── DateUtils.java
│   │   ├── bus/{command,query,event}/
│   │   ├── criteria/                     # Filter, Order, Pagination, Criteria
│   │   └── value_objects/                # Identifier, StringValueObject, IntValueObject, ...
│   └── infrastructure/
│       ├── bus/{command,query,event}/    # InMemoryCommandBus, RabbitMqEventBus, MariaDBEventBus
│       ├── persistence/hibernate/        # HibernateRepository<T>, HibernateCriteriaConverter
│       ├── controller/graphql/           # CustomDGSConfiguration, GraphQLExceptionResolver
│       ├── validation/                   # Validator + reglas tipo Laravel
│       ├── circuit_breaker/
│       └── spring/                       # ApiController, ApiExceptionMiddleware
└── template/                             # Esqueleto vacío para nuevos contextos

src/test/com/jaapec/tenant/                 # Espejo de main, con sufijo `Should` y `Mother`
src/main/resources/
├── application.properties
├── messages.properties                  # i18n de errores
├── schema.sql                            # tabla domain_events
└── .env / .env.example
```

> **Importante:** El paquete del bounded context de planes se llama `plans` en `src/main` pero `plan` (singular) en `src/test`. Mantén ese desfase, está intencional en el repo.

---

## 4. Stack y comandos clave

| Acción | Comando |
|---|---|
| Build | `./gradlew clean build` |
| Run local | `./gradlew bootRun` |
| Tests | `./gradlew test` |
| Cobertura | `./gradlew jacocoTestReport` |
| Formatear | `./gradlew spotlessApply` |
| Levantar deps | `docker-compose up -d` (MariaDB + RabbitMQ) |

Variables de entorno: ver `src/main/resources/.env.example`.

---

## 5. Patrones del proyecto (resumen)

### 5.1 Aggregate Root
- Extiende `com.jaapec.tenant.shared.domain.AggregateRoot`.
- Es `final class`, todos los campos son `private final`.
- Tiene **dos constructores**: el público con todos los VOs, y uno **package-private sin args** que pone todo a `null` (Hibernate lo necesita).
- Factoría estática `create(...)` que construye el agregado y registra el evento de creación.
- Métodos de negocio (`update`, `changeVisibility`, `addPrice`, ...) **devuelven una nueva instancia** y registran su evento.
- Implementa `equals` y `hashCode` excluyendo `createdAt`/`updatedAt`.

### 5.2 Value Object
- Es `final class`.
- Hereda de `Identifier` (UUIDs), `StringValueObject`, `IntValueObject`, `DateTimeValueObject`, etc., **o** define su propio `value` cuando tiene reglas custom (ej. `BillingInterval` con enum interno).
- Constructor público que valida; constructor package-private sin args (`MyVO()` con `value = null`) para Hibernate.
- Método `value()` (no `getValue()`).
- Si valida, lanza una excepción que extiende `DomainError`.

### 5.3 Domain Event
- Hereda de `com.jaapec.tenant.shared.domain.bus.event.DomainEvent`.
- Si un agregado tiene varios eventos parecidos, crea un abstract `XxxDomainEvent` con la forma común y especializa (`XxxCreatedDomainEvent`, `XxxUpdatedDomainEvent`).
- Implementa `eventName()` (snake.case con punto: `plan.created`), `toPrimitives()` y `fromPrimitives(...)`.
- Tiene un constructor package-private sin args para deserialización.

### 5.4 CQRS Command (slice)
Por cada caso de uso de escritura, dentro de `application/<accion>/` viven:
1. `XxxCommand` — `record` que implementa `Command` con tipos primitivos.
2. `XxxCommandHandler` — `@Service`, recibe el caso de uso, mapea primitivos a VOs, llama al caso de uso.
3. `<Verbo><Aggregate>` (ej. `PlanCreator`, `PlanDeleter`, `ChangeVisibility`) — `@Service` con la lógica: carga agregado si aplica, ejecuta operación, persiste, publica eventos.

### 5.5 CQRS Query (slice)
1. `XxxQuery` — `record` que implementa `Query`.
2. `XxxQueryHandler` — `@Service`, mapea a VOs y llama al finder/searcher.
3. `<Aggregate>Finder` o `<Aggregate>Searcher` — `@Service`, devuelve `XxxResponse` o `PaginatedResponse<XxxResponse>`.

### 5.6 Repositorio
- Interface en `domain/` extiende `com.jaapec.tenant.shared.domain.Repository` y declara: `save`, `update`, `delete`, `find`, `matching(Criteria)`, `count(Criteria)`.
- Implementación en `infrastructure/persistence/hibernate/MariaDB<X>Repository` extiende `HibernateRepository<X>` y se anota con `@Transactional` + `@Service`.
- Mapping XML al lado de la implementación: `<Aggregate>.orm.xml` con `<composite-id>` para el VO id y `<component>` para el resto.

### 5.7 Controller GraphQL
- Hereda de `GraphQLApiController` (que extiende `ApiController` y expone `dispatch(Command)` y `ask(Query)`).
- Anotado con `@Controller` y métodos con `@MutationMapping` / `@QueryMapping`.
- Para mutaciones con validación: usa `Validator` con un `Map<String,String> rules` (sintaxis estilo Laravel: `required|not_empty|max:255|min:3`). Lanza `GraphQLExceptionList` si hay errores.
- Define `Request<X>` records en `infrastructure/controller/`.
- Define el schema en `infrastructure/controller/graphql/schema/schema.graphqls` extendiendo `Mutation`/`Query`.

### 5.8 Errores
- Todo error de dominio extiende `DomainError(errorCode, Message, reason, value)`.
- `errorCode` sigue el formato `E<HTTP-like>` (`E404`, `E409`, `E423`, ...).
- `Message.messageKey` apunta a una entrada de `src/main/resources/messages.properties`.
- El `GraphQLExceptionResolver` los traduce a errores GraphQL con `extensions: { code, reason, value }`.

### 5.9 Criteria / Búsqueda
- Toda query de listado usa `Criteria(Filters, Order, Pagination)`.
- Operadores: `EQUAL`, `NOT_EQUAL`, `GT`, `LT`, `CONTAINS`, `NOT_CONTAINS`.
- `Pagination`: `limit` por defecto 20, máximo 100. **Ojo**: el constructor hace `offset = max(offset - 1, 0)` (paginación 1-indexed por fuera, 0-indexed por dentro).
- Se traduce a JPA Criteria en `HibernateCriteriaConverter`.

### 5.10 Tests
- Naming: `<Class>Should.java` (no `<Class>Test`).
- Tres pirámides:
  - **Unit** (`extends UnitTestCase` / `<Module>ModuleUnitTestCase`): mockean `EventBus`, `QueryBus`, `Repository`. Verifican con `shouldHaveSaved`, `shouldHavePublished`.
  - **Infrastructure** (`extends InfrastructureTestCase` / `<Module>ModuleInfrastructureTestCase`): `@SpringBootTest`, real DB, `@Transactional` para rollback.
  - **Application/Feature** (`extends ApplicationTestCase`): `@SpringBootTest + @AutoConfigureMockMvc`, ejecutan GraphQL via `DgsQueryExecutor`.
- Generación de datos: **Object Mothers** en `<bc>/domain/<X>Mother.java` y `<bc>/application/<X>Mother.java`. Toda Mother expone al menos `create(...)` y `random()`. Datos aleatorios via `MotherCreator.random()` (Datafaker) o `UuidMother.generate()`.

---

## 6. Convenciones de código

- **Java 25**, records donde aplica (commands, queries, responses, criteria primitives).
- **Indentación con tabs**, ancho 120 (lo aplica Spotless con prettier-java).
- **Orden de imports**: `\\#`, `java`, `\u200B`, `com.jaapec`, `\u200B` (definido en `build.gradle`).
- **Acceso por `value()`**, no `getValue()`.
- **Clases `final` siempre** que no sean abstractas o estén pensadas para extender.
- **Sin Lombok**. Equals, hashCode y toString se escriben a mano.
- **Sin getters/setters generados.** Métodos de acceso son del estilo `name()`, `id()`, etc.

---

## 7. Convenciones de Git

- **Conventional Commits** (`feat`, `fix`, `chore`, `refactor`, `docs`, `style`, `test`, `perf`, `build`, `revert`).
- Formato: `<type>(<scope>): <descripción imperativa>`. Ej.: `feat(plans): add billing interval validation`.
- **SemVer** en `version.txt` y `build.gradle` (`MAJOR.MINOR.PATCH`).
- Detalles completos en `COMMITS_GUIDE.md` y `VERSIONING.md`.
- No hagas commits a menos que el usuario lo pida explícitamente.

---

## 8. Cuando agregues una feature nueva, sigue este orden

1. **Modelar el dominio:** Value Objects → Aggregate (o método nuevo en agregado existente) → Domain Event.
2. **Caso de uso:** Crear carpeta `application/<accion>/` con Command/Query + Handler + Service.
3. **Persistencia:** Si es agregado nuevo, crea el `MariaDB<X>Repository` y el `<X>.orm.xml`. Si es campo nuevo en agregado existente, agrégalo al `.orm.xml`.
4. **API:** Extiende el `schema.graphqls` del bounded context, crea/extiende el `DataFetcher` correspondiente, crea el `Request<X>` record si necesitas input nuevo.
5. **Tests:** Mothers para los nuevos VOs/eventos → unit test del handler → infrastructure test del repo (si aplica) → feature test del DataFetcher.
6. **Mensajes de error:** Agrega claves nuevas a `messages.properties` y úsalas en tus `DomainError`.
7. **Build & format:** `./gradlew spotlessApply && ./gradlew test`.

---

## 9. Catálogo de skills disponibles

Cuando vayas a trabajar en una tarea concreta, consulta primero la skill correspondiente. Las skills viven en `.claude/skills/<nombre>/SKILL.md` y son **autoritativas sobre los patrones del proyecto**.

| Skill | Cuándo usarla |
|---|---|
| `ddd-architecture-overview` | Antes de empezar cualquier feature, para refrescar la arquitectura completa. |
| `code-style-and-conventions` | Estilo Java, naming, formato, dependencias permitidas. |
| `create-bounded-context-module` | Crear un bounded context completo desde cero copiando `template/`. |
| `create-aggregate-root` | Definir un nuevo Aggregate Root (factory, eventos, equals/hashCode). |
| `create-value-object` | Crear Value Objects (Identifier, StringVO, IntVO, custom con enum). |
| `create-domain-event` | Definir un Domain Event y su jerarquía abstracta si aplica. |
| `create-cqrs-command` | Slice completo de Command (record, Handler, caso de uso, evento, test). |
| `create-cqrs-query` | Slice completo de Query (record, Handler, Finder/Searcher, Response). |
| `create-graphql-controller` | DataFetcher, schema, validación con `Validator`. |
| `create-hibernate-repository` | Implementación MariaDB + archivo `.orm.xml`. |
| `create-domain-error` | DomainError nuevo + clave en `messages.properties`. |
| `write-unit-test` | Tests unitarios de handlers, agregados y VOs con Mothers. |
| `write-feature-test` | Tests GraphQL end-to-end con `ApplicationTestCase` + `DgsQueryExecutor`. |

---

## 10. Qué NO hacer

- No mezclar lógica de dominio en los DataFetchers o en los CommandHandlers (deben ser orquestación pura).
- No exponer entidades de dominio directamente en GraphQL — siempre via `XxxResponse` records.
- No añadir validación de formato/longitud en los VOs cuando ya existe la regla en `Validator` del controller (regla: validación sintáctica en el controller, invariantes de dominio en el VO).
- No publicar eventos directamente desde el agregado. El agregado solo `record(...)`. La publicación la hace siempre el caso de uso de aplicación.
- No suprimir warnings con `@SuppressWarnings` sin justificarlo.
- No introducir librerías de mocking adicionales (solo Mockito) ni assertion libraries más allá de JUnit + AssertJ que ya están.

---

## 11. Referencias rápidas

- Skeleton de un nuevo bounded context: `src/main/com/jaapec/tenant/template/`.
- Bounded context más completo y de referencia: `plans/`.
- Ejemplo de agregado con lógica rica: `tenant/domain/Tenant.java` (suscripciones, dominio custom, etc.).
- Ejemplo de circuit breaker + failover: `shared/infrastructure/bus/event/rabbitmq/RabbitMqEventBus.java`.
