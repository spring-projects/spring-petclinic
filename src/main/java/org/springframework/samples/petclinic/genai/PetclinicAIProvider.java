package org.springframework.samples.petclinic.genai;

import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.samples.petclinic.vet.VetRepository;
import org.springframework.stereotype.Service;

/**
 * Functions that are invoked by the LLM will use this bean to query the system of record
 * for information such as listing owners and vers, or adding pets to an owner.
 *
 * @author Oded Shopen
 */
@Service
@Profile("openai")
public class PetclinicAIProvider {

	OwnerRepository ownerRepository;

	VetRepository vetRepository;

	public PetclinicAIProvider(OwnerRepository ownerRepository, VetRepository vetRepository) {
		this.ownerRepository = ownerRepository;
		this.vetRepository = vetRepository;
	}

	public OwnersResponse getAllOwners() {
		Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
		Page<Owner> ownerPage = ownerRepository.findAll(pageable);
		return new OwnersResponse(ownerPage.getContent());
	}

	public VetResponse getAllVets() {
		Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
		Page<Vet> vetsPage = vetRepository.findAll(pageable);
		return new VetResponse(vetsPage.getContent());
	}

	public AddedPetResponse addPetToOwner(AddPetRequest request) {
		Owner owner = ownerRepository.findById(request.ownerId());
		owner.addPet(request.pet());
		this.ownerRepository.save(owner);
		return new AddedPetResponse(owner);
	}

}
