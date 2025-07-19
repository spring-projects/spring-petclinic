package org.springframework.samples.petclinic.vaccine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.exception.NotFoundException;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.vaccine.predicate.PetPredicates;
import org.springframework.samples.petclinic.vaccine.request.VaccinationRequest;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
public class VaccinationService {

	private static final Logger log = LoggerFactory.getLogger(VaccinationService.class);

	@Autowired
	private VaccinationRepository vaccinationRepository;

	@Autowired
	private VaccineRepository vaccineRepository;

	@Autowired
	private OwnerRepository ownerRepository;

	public Vaccinations addVaccineToPet(Integer ownerId, Integer petId, VaccinationRequest request) {
		var owner = ownerRepository.findByIdAndActive(ownerId, Boolean.TRUE)
			.orElseThrow(() -> new NotFoundException(format("owner not found with id %s", ownerId)));

		var pet = owner.getPets()
			.stream()
			.filter(PetPredicates.isActive().and(PetPredicates.hasId(petId)))
			.findFirst()
			.orElseThrow(() -> new NotFoundException(format("Active pet not found with id %s", petId)));

		var vaccine = vaccineRepository.findById(request.getVaccineId())
			.orElseThrow(() -> new NotFoundException(format("Vaccine not found with id %s", request.getVaccineId())));

		Vaccinations vaccination = new Vaccinations();
		vaccination.setPet(pet);
		vaccination.setVaccine(vaccine);
		vaccination.setDescription(request.getDescription());
		vaccination.setInjected(request.isInjected());
		vaccination.setVaccinationDate(request.getVaccinationDate());
		var savedVaccination = vaccinationRepository.save(vaccination);
		log.info("vaccine schedule saved successfully for pet {} and owner {} ", pet.getId(), ownerId);
		return savedVaccination;
	}

	public Vaccinations updateVaccination(Integer id, VaccinationRequest request) {
		var vaccination = vaccinationRepository.findById(id)
			.orElseThrow(() -> new NotFoundException(format("Vaccination not found with id %s", id)));

		var vaccine = vaccineRepository.findById(request.getVaccineId())
			.orElseThrow(() -> new NotFoundException(format("Vaccination not found with id %s", id)));

		vaccination.setVaccine(vaccine);
		vaccination.setVaccinationDate(request.getVaccinationDate());
		vaccination.setDescription(request.getDescription());
		vaccination.setInjected(request.isInjected());
		var updatedVaccination = vaccinationRepository.save(vaccination);
		log.info("vaccination updated {}", request.getVaccineId());
		return updatedVaccination;

	}

}
