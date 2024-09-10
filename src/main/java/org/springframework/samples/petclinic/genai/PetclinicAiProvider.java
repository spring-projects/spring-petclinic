package org.springframework.samples.petclinic.genai;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.stereotype.Service;

@Service
public class PetclinicAiProvider {

	OwnerRepository ownerRepository;

	public PetclinicAiProvider(OwnerRepository ownerRepository) {
		this.ownerRepository = ownerRepository;
	}

	public OwnersResponse getAllOwners() {
		Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
		Page<Owner> ownerPage = ownerRepository.findAll(pageable);
		return new OwnersResponse(ownerPage.getContent());
	}

	public AddedPetResponse addPetToOwner(AddPetRequest request) {
		Owner owner = ownerRepository.findById(request.ownerId());
		owner.addPet(request.pet());
		this.ownerRepository.save(owner);
		return new AddedPetResponse(owner);
	}

}
