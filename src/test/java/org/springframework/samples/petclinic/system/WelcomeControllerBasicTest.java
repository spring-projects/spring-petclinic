package org.springframework.samples.petclinic.system;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for WelcomeController
 */
public class WelcomeControllerBasicTest {

	@Test
	public void testWelcomeControllerConstruction() {
		WelcomeController controller = new WelcomeController();
		assertNotNull(controller);
	}

	@Test
	public void testWelcomeControllerWelcomeMethod() {
		WelcomeController controller = new WelcomeController();
		String viewName = controller.welcome();
		assertNotNull(viewName);
		assertEquals("welcome", viewName);
	}

	@Test
	public void testWelcomeControllerReturnsString() {
		WelcomeController controller = new WelcomeController();
		Object result = controller.welcome();
		assertInstanceOf(String.class, result);
	}

}
