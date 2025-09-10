package org.springframework.samples.petclinic.owner;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.exception.PetIDExistsException;
import org.springframework.samples.petclinic.exception.PetIdNotFoundException;
import org.springframework.samples.petclinic.model.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/pet-type/features/")
public class PetFeaturesController {

	private final PetFeaturesRepository petFeatures;

	public PetFeaturesController(PetFeaturesRepository petFeatures) {
		this.petFeatures = petFeatures;
	}

	@PostMapping(value = "/add")
	public ResponseEntity<ApiResponse> addPetFeatures(@Valid @RequestBody PetFeatureReqBody petFeatureReqBody)
		throws PetIDExistsException {

		Optional<PetFeatures> petFeatureCheck = petFeatures.findById(petFeatureReqBody.getPetId());
		if(petFeatureCheck.isPresent()) {
			throw new PetIDExistsException("Pet id already exists");
		}

		PetFeatures savePetFeatures = petObject(petFeatureReqBody);

		petFeatures.save(savePetFeatures);
		ApiResponse apiResponse = new ApiResponse("success", "Data Added", LocalDateTime.now());

		return ResponseEntity.ok().body(apiResponse);

	}

	@GetMapping(value = "/all")
	public ResponseEntity<List<PetFeatures>> getAllData(){

		List<PetFeatures> petFeaturesList = petFeatures.findAll();
		return ResponseEntity.ok().body( petFeaturesList );
	}

	@PutMapping(value = "/update")
	public ResponseEntity<ApiResponse> updatePetDetails(@Valid @RequestBody PetFeatureReqBody petFeatureReqBody, @PathVariable int petId)
		throws PetIdNotFoundException {

		Optional.ofNullable(petFeatures.findById(petId)
			.orElseThrow(() -> new PetIdNotFoundException("Pet Id not found while updating: "+ petId)));

		PetFeatures updatePetFeatures = petObject( petFeatureReqBody );
		petFeatures.save(updatePetFeatures);

		ApiResponse apiResponse = new ApiResponse("success", "Data Updated", LocalDateTime.now());
		return ResponseEntity.ok(apiResponse);
	}

	@DeleteMapping(value = "/{petId}")
	public ResponseEntity<ApiResponse> deletePetId(@PathVariable Integer petId) throws PetIdNotFoundException {

		Optional<PetFeatures> deleteObject = Optional.ofNullable(petFeatures.findById(petId)
			.orElseThrow(() -> new PetIdNotFoundException("Pet Id not found while deleting: "+ petId)));

		petFeatures.delete(deleteObject.get());
		ApiResponse apiResponse = new ApiResponse("success", "PetId Deleted", LocalDateTime.now());
		return ResponseEntity.ok(apiResponse);
	}


	private PetFeatures petObject(PetFeatureReqBody petDetails){

		PetFeatures savePetFeatures = new PetFeatures();

		savePetFeatures.setPetId( petDetails.getPetId() );
		savePetFeatures.setTemperament( petDetails.getTemperament() );
		savePetFeatures.setSkinColor( petDetails.getSkinColor() );
		savePetFeatures.setSkinType( petDetails.getSkinType() );
		savePetFeatures.setHeight( petDetails.getHeight() );
		savePetFeatures.setWeight( petDetails.getWeight() );

		return savePetFeatures;
	}

}
