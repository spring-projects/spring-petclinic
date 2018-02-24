package org.springframework.samples.petclinic.owner;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.core.style.ToStringCreator;

public class OwnerTest {

	private Owner instance;

	@Before
	public void setUp() {
		this.instance = new Owner();
	}
	
	
	@Test
	public void getSetTelephoneTest() {
		// Owner instance = new Owner();
		instance.setTelephone("514 371 9999");
		String result = instance.getTelephone();
		assertEquals("514 371 9999", result);
	}
	
	@Test
	public void setGetCityTest() {
		// Owner instance = new Owner();
		instance.setCity("Montreal");
		String result = instance.getCity();
		assertEquals("Montreal", result);
	}
	
	@Test
	public void toStringTest() {
		ToStringCreator creator = new ToStringCreator(instance);
		String expected = 
				creator
				.append("id", instance.getId())
				.append("new", instance.isNew())
				.append("lastName", instance.getLastName())
				.append("firstName", instance.getFirstName())
        			.append("address", instance.getAddress())
                .append("city", instance.getCity())
        			.append("telephone", instance.getTelephone())
				.toString();
		String result = instance.toString();
		assertEquals(expected, result); 
	}
	
	@Test
	public void setPetgetPetsTest() {
		Pet pet = new Pet();
		instance.addPet(pet);
		List<Pet> result = instance.getPets();
		Pet onlyPet = result.iterator().next();

		assertEquals(1, result.size()); // Make sure there's only one element in the Collection returned	
		assertEquals(pet, onlyPet);
	}

}