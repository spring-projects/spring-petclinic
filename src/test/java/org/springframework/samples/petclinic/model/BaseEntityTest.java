/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BaseEntityTest {

	private BaseEntity entity;

	@BeforeEach
	void setUp() {
		entity = new BaseEntity();
	}

	@Test
	void testBaseEntityCreation() {
		assertNotNull(entity);
	}

	@Test
	void testBaseEntityIdSetterGetter() {
		entity.setId(1);
		assertEquals(1, entity.getId());
	}

	@Test
	void testBaseEntityIdNull() {
		assertNull(entity.getId());
	}

	@Test
	void testBaseEntityIsNewWhenIdNull() {
		assertTrue(entity.isNew());
	}

	@Test
	void testBaseEntityIsNotNewWhenIdSet() {
		entity.setId(5);
		assertFalse(entity.isNew());
	}

	@Test
	void testBaseEntityMultipleIdUpdates() {
		entity.setId(1);
		assertEquals(1, entity.getId());
		entity.setId(2);
		assertEquals(2, entity.getId());
	}

	@Test
	void testBaseEntityWithZeroId() {
		entity.setId(0);
		assertEquals(0, entity.getId());
		assertFalse(entity.isNew());
	}

	@Test
	void testBaseEntityWithLargeId() {
		entity.setId(999999);
		assertEquals(999999, entity.getId());
		assertFalse(entity.isNew());
	}

	@Test
	void testBaseEntityIdResetToNull() {
		entity.setId(5);
		assertFalse(entity.isNew());
		entity.setId(null);
		assertTrue(entity.isNew());
	}

	@Test
	void testBaseEntitySerializable() {
		assertNotNull(entity);
		assertTrue(entity instanceof java.io.Serializable);
	}

	@Test
	void testBaseEntityEquality() {
		BaseEntity entity1 = new BaseEntity();
		entity1.setId(1);
		BaseEntity entity2 = new BaseEntity();
		entity2.setId(1);
		assertEquals(entity1.getId(), entity2.getId());
	}

	@Test
	void testBaseEntityDifferentIds() {
		BaseEntity entity1 = new BaseEntity();
		entity1.setId(1);
		BaseEntity entity2 = new BaseEntity();
		entity2.setId(2);
		assertNotEquals(entity1.getId(), entity2.getId());
	}

}
