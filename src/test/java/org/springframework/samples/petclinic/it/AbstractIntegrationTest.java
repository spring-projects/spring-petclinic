package org.springframework.samples.petclinic.it;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Base class for the group's integration tests (*IT). Boots the full application context
 * with the in-memory H2 database and exposes MockMvc. Every test creates its own data
 * with a unique last name, so tests do not depend on order or on each other.
 */
@SpringBootTest
@AutoConfigureMockMvc
abstract class AbstractIntegrationTest {

	@Autowired
	protected MockMvc mockMvc;

	@Autowired
	protected OwnerRepository owners;

	protected static String uniqueLastName() {
		return "Test" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);
	}

	protected Owner saveOwner(String lastName) {
		Owner owner = new Owner();
		owner.setFirstName("Maria");
		owner.setLastName(lastName);
		owner.setAddress("Rua das Flores, 100");
		owner.setCity("Belo Horizonte");
		owner.setTelephone("3133334444");
		return this.owners.saveAndFlush(owner);
	}

}
