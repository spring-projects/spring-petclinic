package org.springframework.samples.petclinic.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BaseEntityTest {

	@Test
	public void testBaseEntityIdSetAndGet() {
		BaseEntity entity = new BaseEntity();
		entity.setId(1);
		assertEquals(1, entity.getId());
	}

	@Test
	public void testBaseEntityIdIsNull() {
		BaseEntity entity = new BaseEntity();
		assertNull(entity.getId());
	}

	@Test
	public void testBaseEntityIsNewWhenIdNull() {
		BaseEntity entity = new BaseEntity();
		assertTrue(entity.isNew());
	}

	@Test
	public void testBaseEntityIsNotNewWhenIdSet() {
		BaseEntity entity = new BaseEntity();
		entity.setId(5);
		assertFalse(entity.isNew());
	}

	@Test
	public void testBaseEntityMultipleIds() {
		BaseEntity entity1 = new BaseEntity();
		BaseEntity entity2 = new BaseEntity();
		entity1.setId(1);
		entity2.setId(2);
		assertNotEquals(entity1.getId(), entity2.getId());
	}

}
