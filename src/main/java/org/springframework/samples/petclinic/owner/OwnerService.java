package org.springframework.samples.petclinic.owner;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.samples.petclinic.Pet.PetType;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OwnerService {

	private final OwnerRepository owners;

	public Owner findOwner(Integer ownerId) {
		Owner owner = this.owners.findById(ownerId);
		Assert.notNull(owner, "Owner ID not found: " + ownerId);
		return owner;
	}

	public List<PetType> findPetTypes() {
		return this.owners.findPetTypes();
	}

	public void saveOwner(@NotNull Owner owner) {
		// add fancy logic here to make this method more useful
		this.owners.save(owner);
	}

}
