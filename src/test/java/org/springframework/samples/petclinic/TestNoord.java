package org.springframework.samples.petclinic;

import org.junit.jupiter.api.Test;

import static junit.framework.TestCase.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

public class TestNoord {
	@Test
	public void simpleTestTrue() {
		assertThat(true).isTrue();
	}

	@Test
	public void simpleTestFalse() {
		assertThat(false).isFalse();
	}

	@Test
	public void simpleTestEquals() {
		assertEquals(3,3);
	}
}
