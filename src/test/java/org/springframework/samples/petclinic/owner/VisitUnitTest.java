
package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VisitUnitTest {

	@Test
	void setDescription_ShouldReturnSameDescription() {
		// Arrange
		Visit visit = new Visit();
		String description = "Routine annual check-up";

		// Act
		visit.setDescription(description);

		// Assert
		assertEquals(description, visit.getDescription());
	}

}
