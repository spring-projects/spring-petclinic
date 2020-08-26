package org.springframework.samples.petclinic.sample;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SampleControllerTest {

	@Test
	public void testDoSomething() {
		SampleRepository sampleRepository = new SampleRepository();
		SampleController sampleController = new SampleController(sampleRepository);
		sampleController.doSomething();
	}

}
