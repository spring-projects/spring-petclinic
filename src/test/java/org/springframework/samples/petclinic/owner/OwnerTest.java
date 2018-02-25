package org.springframework.samples.petclinic.owner;

import static org.junit.Assert.*;

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
		pet.setName("Pogo");
		ownerInstance.addPet(pet);
		List<Pet> result = ownerInstance.getPets();
		Pet onlyPet = result.iterator().next();

		assertEquals(1, result.size()); // Make sure there's only one element in the Collection returned	
		assertEquals(pet, onlyPet);
		assertEquals(pet.getName(), onlyPet.getName());
	}

}