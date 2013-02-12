
package org.springframework.samples.petclinic.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.Vets;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
public class VetController {

	private final ClinicService clinicService;


	@Autowired
	public VetController(ClinicService clinicService) {
		this.clinicService = clinicService;
	}

	@RequestMapping("/vets")
	public String showVetList(Model model) {
		// Here we are returning an object of type 'Vets' rather than a collection of Vet objects 
		// so it is simpler for Object-Xml mapping
		Vets vets = new Vets();
		vets.getVetList().addAll(this.clinicService.findVets());
		model.addAttribute("vets", vets);
		return "vets/vetList";
	}





}
