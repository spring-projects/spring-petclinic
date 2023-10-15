package org.springframework.samples.petclinic;

import org.junit.jupiter.api.Test;

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
}
