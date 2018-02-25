package org.springframework.samples.petclinic.owner;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

public class PetControllerMockito {
	private PetRepository pets;
	private OwnerRepository owners;
	private PetController controller;
	private BindingResult result;
	private ModelMap model;
	
	private Owner owner;
	private Pet pet;
	
	private final String VIEWS_PETS_CREATE_OR_UPDATE_FORM = "pets/createOrUpdatePetForm";
	
	@Before
    public void setup() {
        pets = mock(PetRepository.class);
        owners = mock(OwnerRepository.class);
        result = mock(BindingResult.class);
        model = mock(ModelMap.class);
        owner = mock(Owner.class);
		pet = mock(Pet.class);
        controller = new PetController(pets, owners);
    }
	
	
	//Verifies method calls when a pet already exists
	@Test
	public void testCreationFormError() {
		when(pet.getName()).thenReturn("FIDO");
		when(pet.isNew()).thenReturn(true);
		when(owner.getPet("FIDO", true)).thenReturn(pet);
		when(result.hasErrors()).thenReturn(true);
		
		String redirect = controller.processCreationForm(owner, pet, result, model);
		
		//Pet should be added to owner
		verify(owner).addPet(pet);
		//Should have an error of a pet with duplicate name
		verify(result).rejectValue("name", "duplicate", "already exists");
		//Should set the "pet" of the redirect
		verify(model).put("pet", pet);
		//Should not save the pet
		verify(pets, times(0)).save(anyObject());
		//should redirect to create/update form
		assertEquals(redirect, VIEWS_PETS_CREATE_OR_UPDATE_FORM);
	}
	
	@Test 
	public void testCreationFormSuccess() {
		when(pet.getName()).thenReturn("FIDO");
		when(pet.isNew()).thenReturn(false);
		when(result.hasErrors()).thenReturn(false);
		
		String redirect = controller.processCreationForm(owner, pet, result, model);
		//Should not make an error
		verify(result, times(0)).rejectValue(anyString(), anyString(), anyString());
		//Should save the pet to repository
		verify(pets).save(pet);
		//Should redirect to a pet page
		assertEquals(redirect, "redirect:/owners/{ownerId}");
	}
	
	@Test
	public void testUpdateFormError() {
		when(result.hasErrors()).thenReturn(true);
		
		String redirect = controller.processUpdateForm(pet, result, owner, model);
		//Pet's owner should be set
		verify(pet).setOwner(owner);
		//Should set the "pet" of the redirect
        verify(model).put("pet", pet);
        assertEquals(redirect, VIEWS_PETS_CREATE_OR_UPDATE_FORM);
	}
	
	@Test
	public void testUpdateFormSuccess() {
		when(result.hasErrors()).thenReturn(false);
		
		String redirect = controller.processUpdateForm(pet, result, owner, model);
		//Pet should be added to owner
		verify(owner).addPet(pet);
		//Should save the pet to repository
		verify(pets).save(pet);
		//Should redirect to an owner page
        assertEquals(redirect, "redirect:/owners/{ownerId}");
	}
}
