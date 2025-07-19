package org.springframework.samples.petclinic.vaccine;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.samples.petclinic.vaccine.request.VaccineCreateRequest;
import org.springframework.samples.petclinic.vaccine.request.VaccineRequest;
import org.springframework.samples.petclinic.vaccine.request.VaccineUpdateRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RequestMapping("/vaccine")
@RestController
public class VaccineController {

	@Autowired
	private VaccineService vaccineService;

	@PostMapping
	@ResponseStatus(HttpStatus.OK)
	public Vaccine addVaccine(@RequestBody @Valid VaccineCreateRequest request) {
		return vaccineService.addVaccine(request);
	}

	@PatchMapping("/{id}")
	public Vaccine updateVaccine(@PathVariable Integer id, @RequestBody @Valid VaccineUpdateRequest request) {
		return vaccineService.updateVaccine(request, id);
	}

}
