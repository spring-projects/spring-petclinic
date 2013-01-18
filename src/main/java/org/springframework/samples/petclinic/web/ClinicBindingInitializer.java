package org.springframework.samples.petclinic.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.samples.petclinic.PetType;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

/**
 * Shared WebBindingInitializer for PetClinic's custom editors.
 *
 * <p>Alternatively, such init-binder code may be put into
 * {@link org.springframework.web.bind.annotation.InitBinder}
 * annotated methods on the controller classes themselves.
 *
 * @author Juergen Hoeller
 */
public class ClinicBindingInitializer implements WebBindingInitializer {

	@Autowired
	private ClinicService clinicService;

	public void initBinder(WebDataBinder binder, WebRequest request) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(false));
		binder.registerCustomEditor(PetType.class, new PetTypeEditor(this.clinicService));
	}

}
