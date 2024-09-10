package org.springframework.samples.petclinic.genai;

import java.util.List;
import java.util.function.Function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Profile;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.vet.Vet;

/**
 * This class defines the @Bean functions that the LLM provider will invoke when it
 * requires more Information on a given topic. The currently available functions enable
 * the LLM to get the list of owners and their pets, get information about the
 * veterinarians, and add a pet to an owner.
 *
 * @author Oded Shopen
 */
@Configuration
@Profile("openai")
class AIFunctionConfiguration {

	// The @Description annotation helps the model understand when to call the function
	@Bean
	@Description("List the owners that the pet clinic has")
	public Function<OwnerRequest, OwnersResponse> listOwners(PetclinicAIProvider petclinicAiProvider) {
		return request -> {
			return petclinicAiProvider.getAllOwners();
		};
	}

	@Bean
	@Description("List the veterinarians that the pet clinic has")
	public Function<VetRequest, VetResponse> listVets(PetclinicAIProvider petclinicAiProvider) {
		return request -> {
			return petclinicAiProvider.getAllVets();
		};
	}

	@Bean
	@Description("Add a pet with the specified petTypeId, " + "to an owner identified by the ownerId. "
			+ "The allowed Pet types IDs are only: " + "1 - cat" + "2 - dog" + "3 - lizard" + "4 - snake" + "5 - bird"
			+ "6 - hamster")
	public Function<AddPetRequest, AddedPetResponse> addPetToOwner(PetclinicAIProvider petclinicAiProvider) {
		return request -> {
			return petclinicAiProvider.addPetToOwner(request);
		};
	}

}

record AddPetRequest(Pet pet, String petType, Integer ownerId) {
};

record OwnerRequest(Owner owner) {
};

record OwnersResponse(List<Owner> owners) {
};

record AddedPetResponse(Owner owner) {
};

record VetResponse(List<Vet> vet) {
};

record VetRequest(Vet vet) {

}