package org.springframework.samples.petclinic.model;



import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
public class BaseEntityTests {

	@Test
	public void testGetAndSetId() {
		BaseEntity baseEntity = new BaseEntity();
		baseEntity.setId(1);

		assertThat(baseEntity.getId()).isEqualTo(1);
	}

	@Test
	public void testIsNewWhenIdIsNull() {
		BaseEntity baseEntity = new BaseEntity();

		assertThat(baseEntity.isNew()).isTrue();
	}

	@Test
	public void testIsNewWhenIdIsNotNull() {
		BaseEntity baseEntity = new BaseEntity();
		baseEntity.setId(1);

		assertThat(baseEntity.isNew()).isFalse();
	}
}

