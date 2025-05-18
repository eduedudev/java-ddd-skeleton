package com.jaapec.tenant.shared.domain.value_objects;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EmailShould {

	@Test
	void accept_valid_emails() {
		TestEmail email1 = new TestEmail("test@example.com");
		assertEquals("test@example.com", email1.value());

		TestEmail email2 = new TestEmail("user.name+tag@example.com");
		assertEquals("user.name+tag@example.com", email2.value());

		TestEmail email3 = new TestEmail("user-name@example.co.uk");
		assertEquals("user-name@example.co.uk", email3.value());
	}

	@Test
	void reject_invalid_emails() {
		assertThrows(IllegalArgumentException.class, () -> new TestEmail("invalid-email")); // No @
		assertThrows(IllegalArgumentException.class, () -> new TestEmail("invalid@")); // Sin dominio
		assertThrows(IllegalArgumentException.class, () -> new TestEmail("@invalid.com")); // Sin usuario
		assertThrows(IllegalArgumentException.class, () -> new TestEmail("invalid@.com")); // Dominio comienza con punto
		assertThrows(IllegalArgumentException.class, () -> new TestEmail("invalid@com.")); // Dominio termina con punto
		assertThrows(IllegalArgumentException.class, () -> new TestEmail("invalid@com..com")); // Doble punto en dominio
		assertThrows(IllegalArgumentException.class, () -> new TestEmail("inva lid@example.com")); // Espacio en el usuario
		assertThrows(IllegalArgumentException.class, () -> new TestEmail("invalid@exam ple.com")); // Espacio en dominio
		assertThrows(IllegalArgumentException.class, () -> new TestEmail("invalid@-example.com")); // Dominio inicia con guion
		assertThrows(IllegalArgumentException.class, () -> new TestEmail("invalid@example-.com")); // Dominio termina con guion
		assertThrows(IllegalArgumentException.class, () -> new TestEmail("invalid@.example.com")); // Subdominio inicia con punto
		assertThrows(IllegalArgumentException.class, () -> new TestEmail("invalid@ex..ample.com")); // Doble punto en subdominio
		assertThrows(IllegalArgumentException.class, () -> new TestEmail("invalid@example.c")); // TLD de 1 solo carácter
		assertThrows(IllegalArgumentException.class, () -> new TestEmail("invalid@example.123")); // TLD numérico no válido
		assertThrows(IllegalArgumentException.class, () -> new TestEmail("invalid@#$.com")); // Caracteres inválidos en dominio
	}

	@Test
	void allow_null_value() {
		TestEmail email = new TestEmail(null);
		assertNull(email.value());
	}

	@Test
	void compare_emails_correctly() {
		TestEmail email1 = new TestEmail("test@example.com");
		TestEmail email2 = new TestEmail("test@example.com");
		TestEmail email3 = new TestEmail("different@example.com");

		assertEquals(email1, email2);
		assertNotEquals(email1, email3);
		assertNotEquals(null, email1);

		assertEquals(email1.hashCode(), email2.hashCode());
		assertNotEquals(email1.hashCode(), email3.hashCode());

		TestEmail email4 = new TestEmail("user.name+tag+sorting@example.com");
		TestEmail email5 = new TestEmail("user_name@example.co.uk");
		TestEmail email6 = new TestEmail("user-name@example.travel");
		TestEmail email7 = new TestEmail("USER@EXAMPLE.COM");

		assertNotEquals(email1, email4);
		assertNotEquals(email1, email5);
		assertNotEquals(email1, email6);
		assertNotEquals(email1, email7); // Si no normalizas (lowercase), no son iguales

		assertDoesNotThrow(() -> new TestEmail("user.name+tag+sorting@example.com"));
		assertDoesNotThrow(() -> new TestEmail("user_name@example.co.uk"));
		assertDoesNotThrow(() -> new TestEmail("user-name@example.travel"));
		assertDoesNotThrow(() -> new TestEmail("USER@EXAMPLE.COM"));
	}

	@Test
	void convert_to_string_correctly() {
		TestEmail email = new TestEmail("test@example.com");
		assertEquals("Email{value='test@example.com'}", email.toString());
	}

	@Test
	void email_is_always_lowercase() {
		TestEmail email = new TestEmail("User.Name+TAG@Example.COM");
		assertEquals("user.name+tag@example.com", email.value());
	}

	// Concrete implementation of Email for testing
	static class TestEmail extends Email {

		public TestEmail(String value) {
			super(value);
		}
	}
}
