package org.springframework.samples.petclinic.owner;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.core.style.ToStringCreator;

public class OwnerTest {

	private Owner ownerInstance;

	@Before
	public void setUp() {
		this.ownerInstance = new Owner();
	}
	
	
	@Test
	public void getSetTelephoneTest() {
		// Owner instance = new Owner();
		ownerInstance.setTelephone("514 371 9999");
		String result = ownerInstance.getTelephone();
		assertEquals("514 371 9999", result);
	}
	
	@Test
	public void setGetCityTest() {
		// Owner instance = new Owner();
		ownerInstance.setCity("Montreal");
		String result = ownerInstance.getCity();
		assertEquals("Montreal", result);
	}
	
	@Test
	public void toStringTest() {
		ToStringCreator creator = new ToStringCreator(ownerInstance);
		String expected = 
				creator
				.append("id", ownerInstance.getId())
				.append("new", ownerInstance.isNew())
				.append("lastName", ownerInstance.getLastName())
				.append("firstName", ownerInstance.getFirstName())
        			.append("address", ownerInstance.getAddress())
                .append("city", ownerInstance.getCity())
        			.append("telephone", ownerInstance.getTelephone())
				.toString();
		String result = ownerInstance.toString();
		assertEquals(expected, result); 
	}
	
	@Test
	public void setPetgetPetsTest() {
		Pet pet = new Pet();
		ownerInstance.addPet(pet);
		List<Pet> result = ownerInstance.getPets();
		Pet onlyPet = result.iterator().next();

		assertEquals(1, result.size()); // Make sure there's only one element in the Collection returned	
		assertEquals(pet, onlyPet);
	}

	@Test
	public void getPetExistsTest() {	
		Pet pet = new Pet();
		pet.setName("Pochi");
		ownerInstance.addPet(pet);
		
		//tests pet object exists
		assertEquals(pet, ownerInstance.getPet("Pochi"));	
		assertEquals(pet, ownerInstance.getPet("Pochi", false));
	}
	
	@Test
	public void getPetDoesntExistsTest() {	
		Pet pet = new Pet();
		pet.setName("Pochi");
		ownerInstance.addPet(pet);
		//tests pet object doesn't exist
		assertEquals(null, ownerInstance.getPet("Pochi", true));	
	}
	
	@Test
	public void getPetsTest() {
		Pet pet = new Pet();
		List<Pet> list = new ArrayList<>();
		list.add(pet);
		ownerInstance.addPet(pet);
		
		assertEquals(list, ownerInstance.getPets());
		assertEquals(1, list.size());
		
		Pet pet2 = new Pet();
		list.add(pet2);
		ownerInstance.addPet(pet2);
		
		assertEquals(list, ownerInstance.getPets());
		assertEquals(2, list.size());
	}
	
	@Test
	public void setGetAddress() {
		ownerInstance.setAddress("123 FakeStreet");
		assertEquals("123 FakeStreet", ownerInstance.getAddress());
	}
}