package org.springframework.samples.petclinic.owner;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class OwnerRepositoryTests {

	@Autowired
	private OwnerRepository ownerRepository;

	@Test
	void shouldFetchOwnersWithPetsUsingEntityGraph() {
		Page<Owner> owners =
			ownerRepository.findByLastNameStartingWith("D", PageRequest.of(0, 10));

		assertThat(owners).isNotEmpty();

		Owner owner = owners.getContent().get(0);

		assertThat(owner.getPets()).isNotNull();
	}
}
