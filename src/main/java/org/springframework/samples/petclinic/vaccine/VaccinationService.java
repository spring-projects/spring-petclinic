package org.springframework.samples.petclinic.vaccine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.exception.NotFoundException;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.vaccine.predicate.PetPredicates;
import org.springframework.samples.petclinic.vaccine.request.VaccinationRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
public class VaccinationService {

	@Autowired
	private VaccinationRepository vaccinationRepository;
	private OwnerRepository ownerRepository;



	public void addVaccineToPet(Integer ownerId, Long petId, VaccinationRequest request) {
		var owner = ownerRepository.findByIdAndActive(ownerId, Boolean.TRUE)
			.orElseThrow(()-> new NotFoundException(format("owner not found with id %s", ownerId)));

		var pet = owner.getPets().stream()
			.filter(PetPredicates.isActive().and(PetPredicates.hasId(petId)))
			.findFirst()
			.orElseThrow(() -> new NotFoundException(format("Active pet not found with id %s", petId)));;

			Vaccination vaccination = new Vaccination();
			vaccination.setPet(pet);
			vaccination.setVaccineName("Vacine1");
			vaccination.setDescription("Need to inject");
			vaccination.setVaccinationDate(request.getVaccinationDate());

			pet.addVaccination(vaccination);

			vaccinationRepository.save(vaccination);

	}
}
