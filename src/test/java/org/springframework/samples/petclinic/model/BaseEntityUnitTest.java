package org.springframework.samples.petclinic.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BaseEntityUnitTest {

	private BaseEntity entity;

	@BeforeEach
	void setUp() {
		entity = new BaseEntity();
	}

	@Test
	void isNew_WhenIdIsNull_ReturnsTrue() {
		assertNull(entity.getId(), "L'ID iniziale deve essere null");
		assertTrue(entity.isNew(), "Un'entità senza ID deve risultare nuova");
	}

	@Test
	void isNew_WhenIdIsAssigned_ReturnsFalse() {
		entity.setId(42);

		assertEquals(42, entity.getId());
		assertFalse(entity.isNew(), "Un'entità con ID valorizzato non deve risultare nuova");
	}

}