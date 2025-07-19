package org.springframework.samples.petclinic.vaccine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.exception.NotFoundException;
import org.springframework.samples.petclinic.owner.PetTypeRepository;
import org.springframework.samples.petclinic.vaccine.request.VaccineCreateRequest;
import org.springframework.samples.petclinic.vaccine.request.VaccineUpdateRequest;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
public class VaccineService {

	private static final Logger log = LoggerFactory.getLogger(VaccineService.class);

	@Autowired
	private VaccineRepository vaccineRepository;

	@Autowired
	private PetTypeRepository petTypeRepository;

	public Vaccine addVaccine(VaccineCreateRequest request) {

		var petType = petTypeRepository.findById(request.getPetTypeId())
			.orElseThrow(() -> new NotFoundException(format("Pet type not found with id %s", request.getPetTypeId())));

		Vaccine vaccine = new Vaccine();

		vaccine.setVaccineName(request.getVaccineName());
		vaccine.setPetType(petType);
		vaccineRepository.save(vaccine);
		log.info("vaccine {} saved for pet type {} ", request.getVaccineName(), petType.getName());
		return vaccine;
	}

	public Vaccine updateVaccine(VaccineUpdateRequest request, Integer id) {
		var vaccine = vaccineRepository.findById(id)
			.orElseThrow(() -> new NotFoundException(format("Vaccine not found with id %s", id)));
		vaccine.setVaccineName(request.getVaccineName());
		vaccineRepository.save(vaccine);
		log.info("vaccine {} update successfully...!!!", request.getVaccineName());
		return vaccine;
	}

}
