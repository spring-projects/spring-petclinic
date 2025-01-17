package org.springframework.samples.petclinic.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PersonTests {

	@Test
	public void testSetAndGetFirstName() {
		Person person = new Person();
		String firstName = "John";

		person.setFirstName(firstName);

		assertThat(person.getFirstName()).isEqualTo(firstName);
	}

	@Test
	public void testSetAndGetLastName() {
		Person person = new Person();
		String lastName = "Doe";

		person.setLastName(lastName);

		assertThat(person.getLastName()).isEqualTo(lastName);
	}

	@Test
	public void testDefaultFirstNameAndLastName() {
		Person person = new Person();

		assertThat(person.getFirstName()).isNull();
		assertThat(person.getLastName()).isNull();
	}
}

