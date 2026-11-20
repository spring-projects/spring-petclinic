package org.springframework.samples.petclinic.system;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class CrashControllerUnitTest {

	@Test
	void triggerException_Always_ThrowsRuntimeException() {
		CrashController controller = new CrashController();

		RuntimeException exception = assertThrows(RuntimeException.class, controller::triggerException);

		assertEquals("Expected: controller used to showcase what happens when an exception is thrown",
				exception.getMessage());
	}

}