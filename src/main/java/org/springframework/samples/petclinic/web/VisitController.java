
package org.springframework.samples.petclinic.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.Clinic;
import org.springframework.samples.petclinic.Pet;
import org.springframework.samples.petclinic.Visit;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

/**
 * JavaBean form controller that is used to add a new <code>Visit</code> to the
 * system.
 * 
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
@SessionAttributes("visit")
public class VisitController {

	private final Clinic clinic;


	@Autowired
	public VisitController(Clinic clinic) {
		this.clinic = clinic;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@RequestMapping(value="/owners/*/pets/{petId}/visits/new", method = RequestMethod.GET)
	public String setupForm(@PathVariable("petId") int petId, Model model) {
		Pet pet = this.clinic.findPet(petId);
		Visit visit = new Visit();
		pet.addVisit(visit);
		model.addAttribute("visit", visit);
		return "pets/createOrUpdateVisitForm";
	}

	@RequestMapping(value="/owners/*/pets/{petId}/visits/new", method = RequestMethod.POST)
	public String processSubmit(@Valid Visit visit, BindingResult result, SessionStatus status) {
		if (result.hasErrors()) {
			return "pets/createOrUpdateVisitForm";
		}
		else {
			this.clinic.storeVisit(visit);
			status.setComplete();
			return "redirect:/owners/" + visit.getPet().getOwner().getId();
		}
	}

}
