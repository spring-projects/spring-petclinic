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

import com.fasterxml.jackson.core.JsonProcessingException;

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
	public Function<OwnerRequest, OwnersResponse> listOwners(AIDataProvider petclinicAiProvider) {
		return request -> {
			return petclinicAiProvider.getAllOwners();
		};
	}

	@Bean
	@Description("List the veterinarians that the pet clinic has")
	public Function<VetRequest, VetResponse> listVets(AIDataProvider petclinicAiProvider) {
		return request -> {
			try {
				return petclinicAiProvider.getVets(request);
			}
			catch (JsonProcessingException e) {
				e.printStackTrace();
				return null;
			}
		};
	}

	@Bean
	@Description("Add a pet with the specified petTypeId, " + "to an owner identified by the ownerId. "
			+ "The allowed Pet types IDs are only: " + "1 - cat" + "2 - dog" + "3 - lizard" + "4 - snake" + "5 - bird"
			+ "6 - hamster")
	public Function<AddPetRequest, AddedPetResponse> addPetToOwner(AIDataProvider petclinicAiProvider) {
		return request -> {
			return petclinicAiProvider.addPetToOwner(request);
		};
	}

	@Bean
	@Description("Add a new pet owner to the pet clinic. "
			+ "The Owner must include a first name and a last name as two separate words, "
			+ "plus an address and a 10-digit phone number")
	public Function<OwnerRequest, OwnerResponse> addOwnerToPetclinic(AIDataProvider petclinicAiDataProvider) {
		return request -> {
			return petclinicAiDataProvider.addOwnerToPetclinic(request);
		};
	}

}

record AddPetRequest(Pet pet, Integer ownerId) {
};

record OwnerRequest(Owner owner) {
};

record OwnersResponse(List<Owner> owners) {
};

record OwnerResponse(Owner owner) {
};

record AddedPetResponse(Owner owner) {
};

record VetResponse(List<String> vet) {
};

record VetRequest(Vet vet) {

}