package org.springframework.samples.petclinic.owner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

class PetControllerMoreTest {

	@Test
	void processUpdateForm_duplicateName_returnsForm() {
		OwnerRepository owners = org.mockito.Mockito.mock(OwnerRepository.class);
		PetTypeRepository types = org.mockito.Mockito.mock(PetTypeRepository.class);

		PetController controller = new PetController(owners, types);

		Owner owner = new Owner();
		owner.setId(1);
		Pet existing = new Pet();
		existing.setId(2);
		existing.setName("Buddy");
		// Add directly to the pets collection so the pet is visible to getPet(...) even
		// when not new
		owner.getPets().add(existing);

		Pet pet = new Pet();
		pet.setId(3);
		pet.setName("Buddy");
		pet.setBirthDate(LocalDate.now());

		BeanPropertyBindingResult result = new BeanPropertyBindingResult(pet, "pet");

		String view = controller.processUpdateForm(owner, pet, result, new RedirectAttributesModelMap());

		assertEquals("pets/createOrUpdatePetForm", view);
	}

	@Test
	void processUpdateForm_futureBirthDate_rejectsBirthDate() {
		OwnerRepository owners = org.mockito.Mockito.mock(OwnerRepository.class);
		PetTypeRepository types = org.mockito.Mockito.mock(PetTypeRepository.class);

		PetController controller = new PetController(owners, types);

		Owner owner = new Owner();
		owner.setId(1);

		Pet pet = new Pet();
		pet.setId(5);
		pet.setName("NewPet");
		pet.setBirthDate(LocalDate.now().plusDays(2)); // future date

		BeanPropertyBindingResult result = new BeanPropertyBindingResult(pet, "pet");

		String view = controller.processUpdateForm(owner, pet, result, new RedirectAttributesModelMap());

		assertEquals("pets/createOrUpdatePetForm", view);
	}

	@Test
	void processUpdateForm_noId_throwsIllegalState() {
		OwnerRepository owners = org.mockito.Mockito.mock(OwnerRepository.class);
		PetTypeRepository types = org.mockito.Mockito.mock(PetTypeRepository.class);

		PetController controller = new PetController(owners, types);

		Owner owner = new Owner();
		owner.setId(1);

		Pet pet = new Pet();
		// pet.id left null -> updatePetDetails should assert
		pet.setName("SomePet");
		pet.setBirthDate(LocalDate.now());

		BeanPropertyBindingResult result = new BeanPropertyBindingResult(pet, "pet");

		assertThrows(IllegalStateException.class,
				() -> controller.processUpdateForm(owner, pet, result, new RedirectAttributesModelMap()));
	}

}
