---
name: create-value-object
description: Crear Value Objects siguiendo las jerarquías base (Identifier, StringValueObject, IntValueObject, DateTimeValueObject) o creando custom cuando hay un enum o reglas especiales. Úsala siempre antes de añadir un campo nuevo a un agregado.
---

# Create Value Object

## Tipos base disponibles

| Base | Cuándo | Valor interno |
|---|---|---|
| `Identifier` | UUID v4 | `String` (validado con regex UUID) |
| `StringValueObject` | Texto sin validación | `String` |
| `IntValueObject` | Entero | `Integer` |
| `BigDecimalValueObject` | Importes con decimales no monetarios | `BigDecimal` |
| `BooleanValueObject` | Sí/No | `Boolean` |
| `DateValueObject` | Fecha sin hora | `LocalDate` |
| `DateTimeValueObject` | Timestamp con `LocalDateTime` y conversión a `Timestamp` SQL | `Timestamp` |
| `Email` | Email validado | `String` |

Todos están en `com.jaapec.tenant.shared.domain.value_objects`.

## Reglas

1. **`final class`**.
2. **Dos constructores**:
   - Público con valor (puede validar y lanzar `DomainError`).
   - Package-private sin args con valor en `null` (`MyVO()` con `super(null)` o `this.value = null`). Hibernate lo necesita.
3. **Acceso por `value()`**, nunca `getValue()`.
4. Si la jerarquía base no define `equals`/`hashCode` adecuados, escríbelos manualmente excluyendo nulos correctamente.
5. **Si validas, lanza un `DomainError`**, nunca `IllegalArgumentException` para errores de negocio (excepto en `Identifier`, que es el contrato base de UUID).
6. **El nombre del VO empieza con el nombre del agregado** salvo cuando el concepto es genérico (`Amount`, `Currency`, `BillingInterval` viven en `plans` pero los reusa el agregado `Tenant` al suscribirse).

## Plantillas

### 1) ID basado en UUID

```java
package com.jaapec.tenant.<bc>.domain.value_objects;

import com.jaapec.tenant.shared.domain.value_objects.Identifier;

public final class <X>Id extends Identifier {

	public <X>Id(String value) {
		super(value);
	}

	public <X>Id() {
		super(null);
	}
}
```

### 2) String simple

```java
package com.jaapec.tenant.<bc>.domain.value_objects;

import com.jaapec.tenant.shared.domain.value_objects.StringValueObject;

public final class <X>Name extends StringValueObject {

	public <X>Name(String value) {
		super(value);
	}

	public <X>Name() {
		super(null);
	}
}
```

### 3) Integer con validación de mínimo

```java
package com.jaapec.tenant.<bc>.domain.value_objects;

import com.jaapec.tenant.<bc>.domain.MinValueException;
import com.jaapec.tenant.shared.domain.value_objects.IntValueObject;

public final class <X>MaxFoo extends IntValueObject {

	private static final int MIN = 1;

	public <X>MaxFoo(Integer value) {
		super(ensureMin(value));
	}

	public <X>MaxFoo() {
		super(null);
	}

	private static Integer ensureMin(Integer value) {
		if (value < MIN) throw new MinValueException("MaxFoo", value.toString());
		return value;
	}
}
```

### 4) VO con enum interno

Cuando el dominio tiene un conjunto cerrado de valores (status, visibility, billing interval), no uses `Enum` directo. Crea un VO con un enum anidado:

```java
package com.jaapec.tenant.<bc>.domain.value_objects;

import java.util.Objects;

public final class <X>Status {

	private final String value;

	public <X>Status(String value) {
		Status.valueOf(value);  // valida en construcción
		this.value = value;
	}

	<X>Status() {
		this.value = null;
	}

	public String value() {
		return Status.valueOf(value).toString();
	}

	public enum Status {
		ACTIVE,
		INACTIVE,
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		<X>Status that = (<X>Status) o;
		return Objects.equals(value, that.value);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(value);
	}
}
```

### 5) VO custom con lógica de dominio

Cuando el VO es más que un wrapper, encapsula su lógica:

```java
public final class BillingInterval {
	private final String value;

	public BillingInterval(String value) {
		intervals.valueOf(value.toUpperCase()); // valida
		this.value = value.toUpperCase();
	}

	BillingInterval() { this.value = null; }

	public String value() { return intervals.valueOf(value).toString(); }

	public LocalDateTime calculateExpiration(LocalDateTime from) {
		return switch (intervals.valueOf(value)) {
			case MONTHLY -> from.plusMonths(1);
			case YEARLY -> from.plusYears(1);
		};
	}

	public enum intervals { MONTHLY, YEARLY }

	// equals/hashCode...
}
```

### 6) VO de dominio con validación compleja

Mira `TenantDomain`: valida formato DNS (segmentos, TLD, longitudes). Lanza `InvalidDomainException` si falla. La validación va en un método privado `ensureDomain()` llamado desde el constructor.

## Mapping XML

Todo VO se mapea en el `<Aggregate>.orm.xml` así:

```xml
<component name="status" class="com.jaapec.tenant.<bc>.domain.value_objects.<X>Status" access="field">
    <property name="value" column="status" length="255" access="field" not-null="true"/>
</component>
```

Para el `id`:

```xml
<composite-id name="id" class="com.jaapec.tenant.<bc>.domain.value_objects.<X>Id" access="field">
    <key-property column="id" name="value" access="field" type="string" length="255"/>
</composite-id>
```

## Mother de test

Para cada VO crea un `<X>Mother` en `src/test/.../domain/`:

```java
package com.jaapec.tenant.<bc>.domain;

import com.jaapec.tenant.<bc>.domain.value_objects.<X>Name;
import com.jaapec.tenant.shared.domain.NameMother;

public final class <X>NameMother {

	public static <X>Name create(String value) {
		return new <X>Name(value);
	}

	public static <X>Name random() {
		return create(NameMother.random());
	}
}
```

Para enums:

```java
public final class <X>StatusMother {
	private static final String[] statuses = { "ACTIVE", "INACTIVE" };

	public static <X>Status create(String value) { return new <X>Status(value); }

	public static <X>Status random() {
		return create(statuses[Math.abs(new java.util.Random().nextInt()) % statuses.length]);
	}
}
```

Para IDs UUID:

```java
public final class <X>IdMother {
	public static <X>Id create(String value) { return new <X>Id(value); }
	public static <X>Id random() { return create(UuidMother.generate()); }
}
```

Para enteros con rango:

```java
public final class <X>MaxFooMother {
	public static <X>MaxFoo create(String value) { return new <X>MaxFoo(Integer.parseInt(value)); }
	public static <X>MaxFoo random() {
		return create(String.valueOf(MotherCreator.random().number().numberBetween(1, 100)));
	}
}
```

## Checklist

- [ ] Clase `final` en paquete `<bc>.domain.value_objects` (o `<bc>.domain` para VOs principales como `TenantDomain`, `TenantStatus`).
- [ ] Constructor público (con validación si aplica).
- [ ] Constructor package-private sin args para Hibernate.
- [ ] Acceso vía `value()`.
- [ ] `equals`/`hashCode` correctos (heredados o manuales).
- [ ] Mapping añadido al `.orm.xml`.
- [ ] Mother de test correspondiente.
- [ ] Si valida, hay `DomainError` específico y mensaje en `messages.properties`.
