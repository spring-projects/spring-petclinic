package org.springframework.samples.petclinic.owner;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * new Controller to handle requests relating pet
 *
 * The existing controller is not a REST API controller. So this controller is created to
 * cater Rest endpoints relating Pet.
 *
 * @author Shreyas Prayag
 * @since 1.0
 */
@RestController
@RequestMapping("owners/{ownerId}")
public class NewPetController {

	private final NewPetRepository petRepository;

	NewPetController(NewPetRepository petRepository) {
		this.petRepository = petRepository;
	}

	/**
	 * Retrieves a pet by its ID.
	 * <p>
	 * This endpoint returns a pet based on the provided pet ID. If the pet with the given
	 * ID exists, it returns the pet details in the response body with a 200 OK status. If
	 * the pet is not found, a 404 Not Found status is returned.
	 * </p>
	 * @param petId The ID of the pet to retrieve. It is expected to be a positive
	 * integer.
	 * @return A {@link ResponseEntity} containing the {@link Pet} object if found, or a
	 * 404 response if not found.
	 *
	 * this endpoint is hateoas compliant
	 */
	@GetMapping("/pet/{petId}")
	public ResponseEntity<EntityModel<NewPet>> getPetById(@PathVariable int ownerId, @PathVariable int petId) {

		NewPet pet = petRepository.findById(petId);

		if (pet == null) {
			return ResponseEntity.notFound().build();
		}

		EntityModel<NewPet> petResource = EntityModel.of(pet);

		// Add self link
		petResource.add(linkTo(methodOn(NewPetController.class).getPetById(ownerId, petId)).withSelfRel());

		// Add link to create new pet
		petResource.add(linkTo(methodOn(NewPetController.class).createNewPet(ownerId, null)).withRel("create-new-pet"));

		return ResponseEntity.ok(petResource);

	}

	/**
	 * Creates a new pet.
	 * <p>
	 * This endpoint accepts a {@link Pet} object in the request body and saves it to the
	 * database. It returns the created {@link Pet} object along with a 201 Created HTTP
	 * status code. If the pet data is invalid, a 400 Bad Request status is returned.
	 * </p>
	 * @param newPet The pet object to create. This is expected to contain the pet details
	 * in the request body.
	 * @return A {@link ResponseEntity} containing the created {@link Pet} object and a
	 * 201 Created status.
	 *
	 * this endpoint is hateoas compliant
	 */
	@PostMapping("/pet")
	public ResponseEntity<EntityModel<NewPet>> createNewPet(@PathVariable int ownerId, @RequestBody NewPet newPet) {

		NewPet savedPet = petRepository.save(newPet);
		EntityModel<NewPet> petResource = EntityModel.of(savedPet);

		// Add self link
		petResource.add(linkTo(methodOn(NewPetController.class).getPetById(ownerId, savedPet.getId())).withSelfRel());

		return ResponseEntity.status(HttpStatus.CREATED).body(petResource);

	}

}
