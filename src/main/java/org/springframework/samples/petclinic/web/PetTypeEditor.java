package org.springframework.samples.petclinic.web;

import java.beans.PropertyEditorSupport;

import org.springframework.samples.petclinic.PetType;
import org.springframework.samples.petclinic.service.ClinicService;

/**
 * @author Mark Fisher
 * @author Juergen Hoeller
 */
public class PetTypeEditor extends PropertyEditorSupport {

	private final ClinicService clinicService;


	public PetTypeEditor(ClinicService clinicService) {
		this.clinicService = clinicService;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		for (PetType type : this.clinicService.getPetTypes()) {
			if (type.getName().equals(text)) {
				setValue(type);
			}
		}
	}

}
