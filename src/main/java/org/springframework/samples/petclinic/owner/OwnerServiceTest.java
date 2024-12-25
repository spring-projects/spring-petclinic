package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OwnerServiceTest {

	private OwnerRepository ownerRepository; // Mocked repository

	private OwnerService ownerService; // Service to be tested

	@BeforeEach
	void setup() {
		// Mocking the repository
		ownerRepository = mock(OwnerRepository.class);
		// Injecting the mock into the service
		ownerService = new OwnerService(ownerRepository);
	}

	@Test
	void testFindOwnersByLastNameOnly() {
		// Arrange: Prepare mock return value
		Pageable pageable = PageRequest.of(0, 5);
		Owner owner = new Owner();
		owner.setLastName("Smith");
		Page<Owner> ownersPage = new PageImpl<>(Collections.singletonList(owner));
		when(ownerRepository.findByLastNameStartingWith(eq("Smith"), any(Pageable.class))).thenReturn(ownersPage);

		// Act: Call the service
		Page<Owner> result = ownerService.findOwners("Smith", null, 1);

		// Assert: Verify results
		assertEquals(1, result.getTotalElements()); // Only 1 result is expected
		assertEquals("Smith", result.getContent().get(0).getLastName()); // Verify last
																			// name
	}

	@Test
	void testFindOwnersByLastNameAndCity() {
		// Arrange: Prepare mock return value
		Pageable pageable = PageRequest.of(0, 5);
		Owner owner = new Owner();
		owner.setLastName("Smith");
		owner.setCity("New York");
		Page<Owner> ownersPage = new PageImpl<>(Collections.singletonList(owner));
		when(ownerRepository.findByLastNameAndCity(eq("Smith"), eq("New York"), any(Pageable.class)))
			.thenReturn(ownersPage);

		// Act: Call the service
		Page<Owner> result = ownerService.findOwners("Smith", "New York", 1);

		// Assert: Verify results
		assertEquals(1, result.getTotalElements()); // Only 1 result is expected
		assertEquals("Smith", result.getContent().get(0).getLastName()); // Verify last
																			// name
		assertEquals("New York", result.getContent().get(0).getCity()); // Verify city
	}

	@Test
	void testFindOwnersNoResults() {
		// Arrange: Mock repository to return an empty page
		Pageable pageable = PageRequest.of(0, 5);
		Page<Owner> emptyPage = new PageImpl<>(Collections.emptyList());
		when(ownerRepository.findByLastNameStartingWith(eq("Unknown"), any(Pageable.class))).thenReturn(emptyPage);

		// Act: Call the service
		Page<Owner> result = ownerService.findOwners("Unknown", null, 1);

		// Assert: Verify results
		assertTrue(result.isEmpty()); // No results expected
	}

}
