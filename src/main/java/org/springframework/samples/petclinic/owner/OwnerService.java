package org.springframework.samples.petclinic.owner;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.samples.petclinic.util.DependencyLogger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OwnerService {

	private final OwnerRepository ownerRepository;

	public OwnerService(OwnerRepository ownerRepository) {
		this.ownerRepository = ownerRepository;
	}

	public Page<Owner> findOwners(String lastName, String city, int page) {
		Pageable pageable = PageRequest.of(page - 1, 5);

		if (city == null || city.isEmpty()) {
			DependencyLogger.log("Searching owners by lastName only: " + lastName);
			return ownerRepository.findByLastNameStartingWith(lastName, pageable);
		}
		DependencyLogger.log("Searching owners by lastName and city: " + lastName + ", " + city);
		return ownerRepository.findByLastNameAndCity(lastName, city, pageable);

	}

}
