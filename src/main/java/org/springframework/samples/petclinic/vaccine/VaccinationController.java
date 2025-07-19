package org.springframework.samples.petclinic.vaccine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.vaccine.request.VaccinationRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vaccination")
public class VaccinationController {

	@Autowired
	private VaccinationService vaccinationService;

	@PostMapping("/owner/{owner_id}/pet/{petId}")
	public Vaccinations addVaccine(@PathVariable Integer owner_id, @PathVariable Integer petId,
			@RequestBody VaccinationRequest request) {
		return vaccinationService.addVaccineToPet(owner_id, petId, request);
	}

	@PutMapping("/{id}")
	public Vaccinations updateVaccination(@PathVariable Integer id,
			@RequestBody VaccinationRequest vaccinationRequest) {
		return vaccinationService.updateVaccination(id, vaccinationRequest);
	}

}
