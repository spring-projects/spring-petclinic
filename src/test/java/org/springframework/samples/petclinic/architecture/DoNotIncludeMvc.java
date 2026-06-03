package org.springframework.samples.petclinic.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.core.importer.Location;

public class DoNotIncludeMvc implements ImportOption {

	@Override
	public boolean includes(Location location) {
		return !location.contains("/petclinic/mvc/");
	}

}
