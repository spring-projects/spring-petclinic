package org.springframework.samples.petclinic.domain;

import io.opentelemetry.instrumentation.annotations.WithSpan;

public class UserValidationService {

	@WithSpan
	public boolean vldtUsr(String usr) {

		try {
			Thread.sleep(300);
		}
		catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		return true;
	}

}
