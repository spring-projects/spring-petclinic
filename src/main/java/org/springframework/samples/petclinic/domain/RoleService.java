package org.springframework.samples.petclinic.domain;

import io.opentelemetry.instrumentation.annotations.WithSpan;

public class RoleService {

	@WithSpan
	public boolean vldtUsrRole(String usr, String sysCode) {
		try {
			Thread.sleep(40);
		}
		catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		return true;
	}

}
