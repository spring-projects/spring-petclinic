package org.springframework.samples.petclinic.genai;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.vet.VetRepository;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Functions that are invoked by the LLM will use this bean to query the system of record
 * for information such as listing owners and vers, or adding pets to an owner.
 *
 * @author Oded Shopen
 */
@Service
@Profile("openai")
public class AIDataProvider {

	private final OwnerRepository ownerRepository;

	private final VectorStore vectorStore;

	public AIDataProvider(OwnerRepository ownerRepository, VetRepository vetRepository, VectorStore vectorStore) {
		this.ownerRepository = ownerRepository;
		this.vectorStore = vectorStore;
	}

	public OwnersResponse getAllOwners() {
		Pageable pageable = PageRequest.of(0, 100);
		Page<Owner> ownerPage = ownerRepository.findAll(pageable);
		return new OwnersResponse(ownerPage.getContent());
	}

	public VetResponse getVets(VetRequest request) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		String vetAsJson = objectMapper.writeValueAsString(request.vet());

		SearchRequest sr = SearchRequest.from(SearchRequest.defaults()).withQuery(vetAsJson).withTopK(20);
		if (request.vet() == null) {
			// Provide a limit of 50 results when zero parameters are sent
			sr = sr.withTopK(50);
		}

		List<Document> topMatches = this.vectorStore.similaritySearch(sr);
		List<String> results = topMatches.stream().map(document -> document.getContent()).toList();
		return new VetResponse(results);
	}

	public AddedPetResponse addPetToOwner(AddPetRequest request) {
		Owner owner = ownerRepository.findById(request.ownerId());
		owner.addPet(request.pet());
		this.ownerRepository.save(owner);
		return new AddedPetResponse(owner);
	}

	public OwnerResponse addOwnerToPetclinic(OwnerRequest ownerRequest) {
		ownerRepository.save(ownerRequest.owner());
		return new OwnerResponse(ownerRequest.owner());
	}

}
