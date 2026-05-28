---
name: create-domain-error
description: Crear un DomainError nuevo con su error code, mensaje i18n y mapeo a respuesta GraphQL/REST. Úsala cada vez que un VO o agregado lance una nueva excepción de dominio.
---

# Create Domain Error

## Concepto

Un `DomainError` es una `RuntimeException` con metadatos estructurados que el `GraphQLExceptionResolver` y el `ApiExceptionMiddleware` traducen a una respuesta uniforme.

Cuatro componentes:

| Atributo | Para qué |
|---|---|
| `errorCode` | Código tipo `E404`, `E409`, `E423`. Usado en GraphQL `extensions.code` y como header REST. |
| `errorMessage: Message` | Clave i18n + args. Resuelta con `MessageTranslator`. |
| `reason` | Campo o concepto involucrado. P.ej. `"name"`, `"plan"`, `"domain"`. |
| `value` | Valor que causó el error. P.ej. el nombre duplicado, el id no encontrado. |

## Errores ya disponibles

| Clase | Code | Cuándo usarlo |
|---|---|---|
| `ResourceNotExist(reason, value)` | `E404` | Find que no halla agregado. |
| `ResourceAlreadyExists(reason, uniqueField, value)` | `E409` | Conflicto de unicidad (validación previa al save). |
| `DuplicateFieldException(...)` | — | Reportado por el `Validator` (`unique` rule). |
| `InvalidDomainException(reason, value)` | `E423` | TenantDomain con formato DNS inválido. |
| `MinValueException(reason, value)` | — | Entero por debajo del mínimo. |
| `NonNegativeNumberException(reason, value)` | — | Número negativo donde no se permite. |
| `ActiveSubscriptionAlreadyExistsException()` | — | Tenant ya tiene suscripción activa. |
| `PendingSubscriptionExistsException()` | — | Suscripción pendiente de pago bloquea otra. |
| `SubscriptionNotFound()` | — | Cancelar/activar una suscripción inexistente. |
| `SubscriptionAlreadyActive()` | — | Activar una ya activa. |
| `SubscriptionIsInactiveException()` | — | Operar sobre una inactiva. |

**Antes de crear uno nuevo, revisa si alguno encaja.**

## Plantilla — Error con razón y valor

```java
package com.jaapec.tenant.<bc>.domain;

import com.jaapec.tenant.shared.domain.DomainError;
import com.jaapec.tenant.shared.domain.Message;

public final class <Razon>Exception extends DomainError {

	private static final String ERROR_CODE = "E422";
	private static final String MESSAGE_KEY = "error.<area>.<detalle>";

	public <Razon>Exception(String reason, String value) {
		super(ERROR_CODE, new Message(MESSAGE_KEY, new Object[] { reason }), reason, value);
	}
}
```

## Plantilla — Error sin payload

```java
public final class <X>Exception extends DomainError {

	private static final String ERROR_CODE = "E422";
	private static final String MESSAGE_KEY = "error.<area>.<detalle>";

	public <X>Exception() {
		super(ERROR_CODE, new Message(MESSAGE_KEY, new Object[] {}));
	}
}
```

## Mensaje i18n

Toda excepción referencia una clave en `src/main/resources/messages.properties`. Usa el patrón:

```
error.<area>.<sub_area>.<detalle>=Texto con placeholders {0}, {1}
```

Ejemplos del repo:

```
error.resource.not.found=The {0} doesn't exist
error.resource.exists=The {0} with {1} {2} already exists
error.resource.negative=The {0} must be a non-negative number
error.invalid.domain=The {0} is not a valid domain
error.subscription.active.already.exists=There is already an active subscription for this tenant
error.min.invalid=Field {0} must be less than or equal to {1}
error.required.invalid=The field {0} is required
```

Los placeholders `{N}` se rellenan con los `args` del `Message`.

## Códigos de error sugeridos

| Code | Significado |
|---|---|
| `E400` | Bad Request genérico |
| `E404` | Not Found |
| `E409` | Conflict (unicidad) |
| `E422` | Unprocessable Entity (validación de negocio) |
| `E423` | Locked / dato malformado específico |

Mantén la convención de tres dígitos al estilo HTTP.

## Cómo se traduce al cliente

### En GraphQL (caso normal)

`GraphQLExceptionResolver` produce:

```json
{
  "errors": [{
    "message": "The plan with name Pro already exists",
    "locations": [...],
    "path": ["createPlan"],
    "extensions": {
      "code": "E409",
      "reason": "name",
      "value": "Pro",
      "classification": "BAD_REQUEST"
    }
  }]
}
```

### Errores de validación (Validator)

Múltiples errores agrupados en `GraphQLExceptionList`:

```json
{
  "errors": [
    {
      "message": "The field name is required",
      "extensions": { "field": "name", "classification": "BAD_REQUEST" }
    },
    {
      "message": "The field maxUsers must be greater than or equal to 1",
      "extensions": { "field": "maxUsers", "classification": "BAD_REQUEST" }
    }
  ]
}
```

### En REST (si llegas a usarlo)

`ApiExceptionMiddleware` produce:

```json
{
  "error": true,
  "code": "E409",
  "data": {
    "message": "The plan with name Pro already exists",
    "reason": "name",
    "value": "Pro"
  }
}
```

con el HTTP status mapeado por `RestApiController.errorMapping()`.

## Patrón típico desde el VO

```java
public final class Amount extends IntValueObject {
	private static final int MIN = 0;

	public Amount(Integer value) {
		super(ensureValid(value));
	}

	private static Integer ensureValid(Integer value) {
		if (value < MIN) throw new NonNegativeNumberException("Amount", value.toString());
		return value;
	}
}
```

## Patrón típico desde el caso de uso

```java
public void create(<X>Id id, <X>Name name) {
	if (repository.uniqueField("name", name.value())) {
		throw new ResourceAlreadyExists("<bc>", "name", name.value());
	}
	// ...
}

public void update(<X>Id id, <X>Name name) {
	<X> aggregate = repository.find(id)
		.orElseThrow(() -> new ResourceNotExist("<bc>", id.value()));
	// ...
}
```

## Patrón típico desde el agregado

```java
public Tenant subscribeToPlan(...) {
	if (currentSubs.stream().anyMatch(TenantPlanSubscription::isActive)) {
		throw new ActiveSubscriptionAlreadyExistsException();
	}
	// ...
}
```

## Test del DomainError

No es estrictamente necesario testear el constructor de un DomainError. Sí asegúrate de que el test del handler/agregado verifica que se lanza:

```java
@Test
void throw_when_<condicion>() {
	assertThrows(<X>Exception.class, () -> handler.handle(invalidCommand));
}
```

Y en feature tests:

```java
assertErrorResponse(
	mutationGraphQL,
	variables,
	"The plan with name Pro already exists",
	Map.of("code", "E409", "reason", "name", "value", "Pro")
);
```

## Checklist

- [ ] La excepción extiende `DomainError`, está en `<bc>/domain/` (o `shared/domain/` si es transversal).
- [ ] Es `final`.
- [ ] `ERROR_CODE` definido como `private static final` con formato `E<3 dígitos>`.
- [ ] `MESSAGE_KEY` apunta a una clave existente en `messages.properties`.
- [ ] `messages.properties` actualizado con la nueva clave si es nueva.
- [ ] Constructor pasa los `args` correctos al `Message`.
- [ ] Si tiene `reason`/`value`, ambos son no-null y semánticos.
- [ ] Los tests verifican que se lanza en las situaciones esperadas.

## Referencias

- Base: `shared/domain/DomainError.java`, `shared/domain/Message.java`.
- Errores estándar: `shared/domain/ResourceNotExist.java`, `shared/domain/ResourceAlreadyExists.java`.
- Error custom: `tenant/domain/InvalidDomainException.java`.
- Errores sin payload: `tenant/domain/ActiveSubscriptionAlreadyExistsException.java`.
- Resolver GraphQL: `shared/infrastructure/controller/graphql/GraphQLExceptionResolver.java`.
- Middleware REST: `shared/infrastructure/spring/ApiExceptionMiddleware.java`.
- i18n: `src/main/resources/messages.properties`.
