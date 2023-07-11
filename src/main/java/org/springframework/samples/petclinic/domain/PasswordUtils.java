package org.springframework.samples.petclinic.domain;

import io.opentelemetry.instrumentation.annotations.WithSpan;

public class PasswordUtils {

	@WithSpan
	public boolean vldtPswd(String usr, String pswd) {
		try {
			Thread.sleep(1);
		}
		catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		return true;
	}

	@WithSpan
	public String encPswd(String pswd) {
		try {
			Thread.sleep(300);
		}
		catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		return "";
	}

}
