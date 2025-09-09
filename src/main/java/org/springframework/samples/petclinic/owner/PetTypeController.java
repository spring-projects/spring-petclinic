package org.springframework.samples.petclinic.owner;

import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.model.PetTypeModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pet-type/")
public class PetTypeController {

	private final PetTypeRepository petTypes;

	public PetTypeController(PetTypeRepository petTypes) {
		this.petTypes = petTypes;
	}


	@PostMapping(value = "/add")
	public ResponseEntity<String> addPetTypes(@RequestBody PetTypeModel petType){

		try{
			PetType newPetType = petObject(petType);

			PetType petTypeModel = petTypes.save(newPetType);
			System.out.println(petTypeModel);

			return ResponseEntity.badRequest().body("Pet Added");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}


	@GetMapping(value = "/{petId}")
	public ResponseEntity<PetType> getPetType(@PathVariable("petId") int petId){


		Optional<PetType> optionalPetType = Optional.ofNullable(petTypes.findById(petId).orElseThrow(() -> new IllegalArgumentException("Pet Id not found with Id: " + petId)));
		if(optionalPetType.isPresent()) return ResponseEntity.ok().body(optionalPetType.get());
//		else return ResponseEntity.badRequest();
		return null;
	}

	@PutMapping(value = "/{petId}")
	public void updatePetTypes(@RequestBody PetTypeModel petTypeModel, @PathVariable("petId") int petId  ){

		System.out.println( petTypeModel );
		Optional<PetType> optionalPetType = Optional.ofNullable(petTypes.findById(petId).orElseThrow(() -> new IllegalArgumentException("Pet Id not found with Id: " + petId)));
		if(optionalPetType.isPresent()){

			PetType updatedPetType = petObject(petTypeModel);
			updatedPetType.setId(petId);

			petTypes.save(updatedPetType);
		}
	}

	@DeleteMapping(value = "/{petId}")
	public ResponseEntity<String> deletePetType(@PathVariable("petId") int petId){

		petTypes.deleteById(petId);
		return ResponseEntity.ok().body("PetType with Pet Id: "+petId+" is removed");
	}

	@GetMapping(value = "/all-pet-types")
	public ResponseEntity<List<PetType>> getAllPetTypes(){

		return ResponseEntity.ok().body(petTypes.findAll());
	}


	private PetType petObject(PetTypeModel petTypeModel){

		PetType petType = new PetType();

		petType.setName(petTypeModel.getName());
		petType.setBreed(petTypeModel.getBreed());
		petType.setWeight(petTypeModel.getWeight());
		petType.setHeight(petTypeModel.getHeight());
		petType.setSkinType(petTypeModel.getSkinType());

		petType.setSkinColor(petTypeModel.getSkinColor());
		petType.setTemperament(petTypeModel.getTemperament());
		petType.setLifespan(petTypeModel.getLifespan());

		return petType;
	}


}
