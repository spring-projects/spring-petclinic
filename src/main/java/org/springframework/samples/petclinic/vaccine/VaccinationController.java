package org.springframework.samples.petclinic.vaccine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.PetTypeRepository;
import org.springframework.samples.petclinic.vaccine.request.VaccinationRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vaccine")
public class VaccinationController {

	private VaccinationService vaccinationService;

	@PostMapping("/owner/{owner_id}/pet/{petId}")
	public void addVaccine(@PathVariable Integer owner_id, @PathVariable Long petId,
	@RequestBody VaccinationRequest request){

		vaccinationService.addVaccineToPet(owner_id, petId, request);
	}
}
