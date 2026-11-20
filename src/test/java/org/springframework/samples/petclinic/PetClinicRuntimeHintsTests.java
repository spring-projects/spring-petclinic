
package org.springframework.samples.petclinic;

import org.junit.jupiter.api.Test;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.predicate.RuntimeHintsPredicates;
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.model.Person;
import org.springframework.samples.petclinic.vet.Vet;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PetClinicRuntimeHintsTests {

	@Test
	void shouldRegisterResourceHints() {
		RuntimeHints hints = new RuntimeHints();

		new PetClinicRuntimeHints().registerHints(hints, getClass().getClassLoader());

		assertTrue(RuntimeHintsPredicates.resource().forResource("db/schema.sql").test(hints));

		assertTrue(RuntimeHintsPredicates.resource().forResource("db/h2/schema.sql").test(hints));

		assertTrue(RuntimeHintsPredicates.resource().forResource("messages/messages.properties").test(hints));

		assertTrue(RuntimeHintsPredicates.resource().forResource("mysql-default-conf").test(hints));
	}

	@Test
	void shouldRegisterSerializationHints() {
		RuntimeHints hints = new RuntimeHints();

		new PetClinicRuntimeHints().registerHints(hints, getClass().getClassLoader());

		assertTrue(RuntimeHintsPredicates.reflection().onJavaSerialization(BaseEntity.class, true).test(hints));

		assertTrue(RuntimeHintsPredicates.reflection().onJavaSerialization(Person.class, true).test(hints));

		assertTrue(RuntimeHintsPredicates.reflection().onJavaSerialization(Vet.class, true).test(hints));
	}

}
