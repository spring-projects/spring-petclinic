package org.springframework.samples.petclinic.config;

import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

public class PetClinicRuntimeHints implements RuntimeHintsRegistrar {

	@Override
	public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
		hints.resources().registerPattern("db/*");
		hints.resources().registerPattern("messages/*");
		hints.resources().registerPattern("mysql-default-conf");
	}

}
