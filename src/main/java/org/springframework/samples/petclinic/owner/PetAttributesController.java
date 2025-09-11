package org.springframework.samples.petclinic.owner;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.constants.ApiResponseConstants;
import org.springframework.samples.petclinic.exception.PetIDExistsException;
import org.springframework.samples.petclinic.exception.PetIdNotFoundException;
import org.springframework.samples.petclinic.model.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/pets/{petid}/attributes")
public class PetAttributesController {

	private final PetAttributesRepository petFeatures;

	public PetAttributesController(PetAttributesRepository petFeatures) {
		this.petFeatures = petFeatures;
	}

	@PostMapping
	public ResponseEntity<ApiResponse> addPetAttributes(@Valid @RequestBody PetAttributesReqBody petFeatureReqBody,
														@PathVariable Integer petid)
		throws PetIDExistsException {

		Optional<PetAttributes> petFeatureCheck = petFeatures.findById(petid);
		if(petFeatureCheck.isPresent()) {
			throw new PetIDExistsException( String.valueOf(petid) );
		}

		PetAttributes savePetFeatures = petObject(petFeatureReqBody, petid);

		petFeatures.save(savePetFeatures);
		ApiResponse apiResponse = new ApiResponse(ApiResponseConstants.STATUS_SUCCESS,
			ApiResponseConstants.PET_ATTRIBUTES_ADDED,
			LocalDateTime.now());

		return ResponseEntity.ok().body(apiResponse);

	}

	@GetMapping
	public ResponseEntity<PetAttributes> getPetData(@PathVariable Integer petid) throws PetIdNotFoundException {

		Optional<PetAttributes> petFeaturesList = Optional.ofNullable(petFeatures.findById(petid)
			.orElseThrow(() -> new PetIdNotFoundException( String.valueOf(petid) )));

		return ResponseEntity.ok().body( petFeaturesList.get() );
	}

	@PutMapping
	public ResponseEntity<ApiResponse> updatePetDetails(@Valid @RequestBody PetAttributesReqBody petFeatureReqBody,
														@PathVariable Integer petid)
		throws PetIdNotFoundException {

		Optional.ofNullable(petFeatures.findById( petid )
			.orElseThrow(() -> new PetIdNotFoundException( String.valueOf(petid))));

		PetAttributes updatePetFeatures = petObject( petFeatureReqBody, petid );
		petFeatures.save(updatePetFeatures);

		ApiResponse apiResponse = new ApiResponse(ApiResponseConstants.STATUS_SUCCESS,
			ApiResponseConstants.PET_ATTRIBUTES_UPDATED, LocalDateTime.now());
		return ResponseEntity.ok(apiResponse);
	}

	@DeleteMapping
	public ResponseEntity<ApiResponse> deletePetId(@PathVariable Integer petid) throws PetIdNotFoundException {

		Optional<PetAttributes> deleteObject = Optional.ofNullable(petFeatures.findById(petid)
			.orElseThrow(() -> new PetIdNotFoundException( String.valueOf(petid) )));

		petFeatures.delete(deleteObject.get());
		ApiResponse apiResponse = new ApiResponse(ApiResponseConstants.STATUS_SUCCESS,
			ApiResponseConstants.PET_ATTRIBUTES_DELETED, LocalDateTime.now());
		return ResponseEntity.ok(apiResponse);
	}


	private PetAttributes petObject(PetAttributesReqBody petDetails, Integer petid){

		PetAttributes savePetFeatures = new PetAttributes();

		savePetFeatures.setPetId( petid );
		savePetFeatures.setTemperament( petDetails.getTemperament() );

		savePetFeatures.setHeight( petDetails.getHeight() );
		savePetFeatures.setWeight( petDetails.getWeight() );

		return savePetFeatures;
	}

}
