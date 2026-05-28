---
name: create-hibernate-repository
description: Implementar el adaptador MariaDB de un Repository y crear el archivo .orm.xml correspondiente. Úsala cuando agregues un nuevo agregado o cuando agregues campos persistidos a uno existente.
---

# Create Hibernate Repository (MariaDB)

## Estructura

```
<bc>/domain/<X>Repository.java                         # interface (puerto)
<bc>/infrastructure/persistence/hibernate/
├── MariaDB<X>Repository.java                          # adaptador
└── <X>.orm.xml                                        # mapping XML
```

Si el agregado tiene una entity hija (ej. `Plan` → `PlanPrice`), también:

```
└── <Y>.orm.xml                                        # mapping de la entity
```

## La interfaz

```java
package com.jaapec.tenant.<bc>.domain;

import java.util.List;
import java.util.Optional;

import com.jaapec.tenant.<bc>.domain.value_objects.<X>Id;
import com.jaapec.tenant.shared.domain.Repository;
import com.jaapec.tenant.shared.domain.criteria.Criteria;

public interface <X>Repository extends Repository {
	void save(<X> aggregate);
	void delete(<X> aggregate);
	void update(<X> aggregate);
	Optional<<X>> find(<X>Id id);
	List<<X>> matching(Criteria criteria);
	long count(Criteria criteria);
}
```

`Repository` ya define `boolean uniqueField(String fieldName, String value)`, no lo declares aquí.

## El adaptador

```java
package com.jaapec.tenant.<bc>.infrastructure.persistence.hibernate;

import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;
import org.hibernate.SessionFactory;

import com.jaapec.tenant.<bc>.domain.<X>;
import com.jaapec.tenant.<bc>.domain.<X>Repository;
import com.jaapec.tenant.<bc>.domain.value_objects.<X>Id;
import com.jaapec.tenant.shared.domain.Service;
import com.jaapec.tenant.shared.domain.criteria.Criteria;
import com.jaapec.tenant.shared.infrastructure.persistence.hibernate.HibernateRepository;

@Transactional
@Service
public class MariaDB<X>Repository extends HibernateRepository<<X>> implements <X>Repository {

	public MariaDB<X>Repository(SessionFactory sessionFactory) {
		super(sessionFactory, <X>.class);
	}

	@Override
	public void save(<X> aggregate) { persist(aggregate); }

	@Override
	public void delete(<X> aggregate) { remove(aggregate); }

	@Override
	public void update(<X> aggregate) { merge(aggregate); }

	@Override
	public Optional<<X>> find(<X>Id id) { return byId(id); }

	@Override
	public List<<X>> matching(Criteria criteria) { return byCriteria(criteria); }

	@Override
	public long count(Criteria criteria) { return countByCriteria(criteria); }

	@Override
	public boolean uniqueField(String fieldName, String value) {
		return isFieldValueUnique(fieldName, value);
	}
}
```

> Nota: la clase **no es `final`** (es la única excepción a la regla del proyecto, porque Hibernate puede proxy-arla en algunos escenarios). Sí es `@Transactional` y usa el `@Service` del proyecto.

## El mapping XML

Ubicación: `<bc>/infrastructure/persistence/hibernate/<X>.orm.xml`.

### Plantilla básica

```xml
<?xml version = "1.0" encoding = "utf-8"?>
<!DOCTYPE hibernate-mapping PUBLIC
        "-//Hibernate/Hibernate Mapping DTD//EN"
        "http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd">
<hibernate-mapping>
    <class name="com.jaapec.tenant.<bc>.domain.<X>" table="<x>s">

        <composite-id name="id" class="com.jaapec.tenant.<bc>.domain.value_objects.<X>Id" access="field">
            <key-property column="id" name="value" access="field" type="string" length="255"/>
        </composite-id>

        <component name="name" class="com.jaapec.tenant.<bc>.domain.value_objects.<X>Name" access="field">
            <property name="value" column="name" length="255" access="field" not-null="true" unique="true"/>
        </component>

        <component name="status" class="com.jaapec.tenant.<bc>.domain.value_objects.<X>Status" access="field">
            <property name="value" column="status" length="255" access="field" not-null="true"/>
        </component>

        <component name="createdAt" class="com.jaapec.tenant.<bc>.domain.value_objects.<X>CreatedAt" access="field">
            <property name="value" column="created_at" length="255" access="field" not-null="true"/>
        </component>

        <component name="updatedAt" class="com.jaapec.tenant.<bc>.domain.value_objects.<X>UpdatedAt" access="field">
            <property name="value" column="updated_at" length="255" access="field" not-null="true"/>
        </component>

    </class>
</hibernate-mapping>
```

### Con colección de entities (one-to-many)

```xml
<list name="prices" cascade="all" access="field" fetch="join" lazy="false" inverse="true">
    <key column="plan_id" not-null="true"/>
    <list-index column="list_order"/>
    <one-to-many class="com.jaapec.tenant.<bc>.domain.<Y>"/>
</list>
```

Y en el `<Y>.orm.xml`:

```xml
<many-to-one name="<x>" class="com.jaapec.tenant.<bc>.domain.<X>" fetch="join" lazy="false" access="field">
    <column name="<x>_id" not-null="true"/>
</many-to-one>
```

Mira `Plan.orm.xml` y `Prices.orm.xml`.

### Convenciones

- **Tabla en plural snake_case**: `plans`, `tenants`, `domain_events`.
- **Columnas en snake_case**: `max_users`, `created_at`, `custom_domain`.
- **Boolean / null permitidos**: `not-null="false"` y `length="1"` para `BOOLEAN`/`CHAR(1)`.
- **`access="field"` siempre.** El proyecto no usa setters.
- **`type="string"` solo para `<key-property>`**. Para `<property>` Hibernate lo deduce.

## Configuración de Hibernate

`HibernateConfiguration` (en `shared/infrastructure/persistence/hibernate/`) descubre automáticamente todos los `*.orm.xml` ubicados en `**/infrastructure/persistence/hibernate/`. **No tienes que registrarlos manualmente**.

Propiedades clave:
- `hbm2ddl.auto = update` (modifica el schema automáticamente, está bien para dev).
- `show_sql` controlado por la env `DEBUG=true|false`.

## Test de infraestructura

Ubicación: `src/test/.../<bc-test>/infrastructure/persistence/hibernate/MariaDB<X>RepositoryShould.java`.

```java
package com.jaapec.tenant.<bc-test>.infrastructure.persistence.hibernate;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import com.jaapec.tenant.<bc-test>.<X>ModuleInfrastructureTestCase;
import com.jaapec.tenant.<bc-test>.domain.<X>IdMother;
import com.jaapec.tenant.<bc-test>.domain.<X>Mother;
import com.jaapec.tenant.<bc>.domain.<X>;

@Transactional
class MariaDB<X>RepositoryShould extends <X>ModuleInfrastructureTestCase {

	@Test
	void save_a_<bc>() {
		<X> aggregate = <X>Mother.random();
		assertDoesNotThrow(() -> mariadb<X>Repository.save(aggregate));
	}

	@Test
	void return_an_existing_<bc>() {
		<X> aggregate = <X>Mother.random();
		mariadb<X>Repository.save(aggregate);

		Optional<<X>> found = mariadb<X>Repository.find(aggregate.id());
		assertTrue(found.isPresent());
		assertEquals(aggregate.id(), found.get().id());
		assertEquals(aggregate.name(), found.get().name());
	}

	@Test
	void not_return_a_non_existing_<bc>() {
		assertFalse(mariadb<X>Repository.find(<X>IdMother.random()).isPresent());
	}

	@Test
	void delete_an_existing_<bc>() {
		<X> aggregate = <X>Mother.random();
		mariadb<X>Repository.save(aggregate);
		assertDoesNotThrow(() -> mariadb<X>Repository.delete(aggregate));
		assertFalse(mariadb<X>Repository.find(aggregate.id()).isPresent());
	}
}
```

Y la base del módulo de test:

```java
public abstract class <X>ModuleInfrastructureTestCase extends InfrastructureTestCase {
	@Autowired
	protected <X>Repository mariadb<X>Repository;
}
```

`@Transactional` en el test rolea back los cambios al final. Esto requiere que `docker-compose up -d` esté corriendo con MariaDB.

## Checklist

- [ ] Interface `<X>Repository` en `domain/`.
- [ ] Adaptador `MariaDB<X>Repository` en `infrastructure/persistence/hibernate/` con `@Transactional + @Service`.
- [ ] `<X>.orm.xml` con `<composite-id>` para el VO id y `<component>` para cada VO.
- [ ] Si hay entity hija: `<Y>.orm.xml` y `<list>` en el padre.
- [ ] Tabla y columnas en snake_case plural.
- [ ] Test `MariaDB<X>RepositoryShould` con save/find/delete.
- [ ] `<X>ModuleInfrastructureTestCase` que autoinyecta el repo.

## Referencias

- Adaptador: `plans/infrastructure/persistence/hibernate/MariaDBPlanRepository.java`.
- Mapping con relación 1-N: `Plan.orm.xml` + `Prices.orm.xml`.
- Mapping con BOOLEAN nullable: `Tenant.orm.xml` (`domain_verified` length=1).
- Base test: `shared/infrastructure/persistence/hibernate/HibernateRepository.java`.
- Test de repo: `MariaDBPlanRepositoryShould.java`.
