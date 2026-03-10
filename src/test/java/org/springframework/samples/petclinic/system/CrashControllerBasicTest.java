package org.springframework.samples.petclinic.system;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for CrashController
 */
public class CrashControllerBasicTest {

	@Test
	public void testCrashControllerConstruction() {
		CrashController controller = new CrashController();
		assertNotNull(controller);
	}

	@Test
	public void testCrashControllerTriggerException() {
		CrashController controller = new CrashController();
		try {
			controller.triggerException();
			fail("Expected RuntimeException to be thrown");
		}
		catch (RuntimeException e) {
			assertNotNull(e.getMessage());
		}
	}

	@Test
	public void testCrashControllerExceptionMessage() {
		CrashController controller = new CrashController();
		try {
			controller.triggerException();
		}
		catch (RuntimeException e) {
			assertTrue(e.getMessage().length() > 0);
		}
	}

}
