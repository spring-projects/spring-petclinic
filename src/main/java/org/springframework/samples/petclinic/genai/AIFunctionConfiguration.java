package org.springframework.samples.petclinic.genai;

import java.util.List;
import java.util.function.Function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.Pet;

@Configuration
class AIFunctionConfiguration {

	@Bean
	@Description("List the owners that the pet clinic has")
	public Function<OwnerRequest, OwnersResponse> listOwners(PetclinicAiProvider petclinicAiProvider) {
		return request -> {
			return petclinicAiProvider.getAllOwners();
		};
	}

	@Bean
	@Description("Add a pet to an owner identified by the ownerId")
	public Function<AddPetRequest, AddedPetResponse> addPetToOwner(PetclinicAiProvider petclinicAiProvider) {
		return request -> {
			return petclinicAiProvider.addPetToOwner(request);
		};
	}

}

record AddPetRequest(Pet pet, Integer ownerId) {
};

record OwnerRequest(Owner owner) {
};

record OwnersResponse(List<Owner> owners) {
};

record AddedPetResponse(Owner owner) {
};
