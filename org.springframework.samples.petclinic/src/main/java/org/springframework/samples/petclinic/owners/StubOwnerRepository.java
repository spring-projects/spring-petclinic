package org.springframework.samples.petclinic.owners;

import java.util.Collection;

import org.springframework.stereotype.Repository;

@Repository
public class StubOwnerRepository implements OwnerRepository {

	public Collection<Owner> findOwnersByLastName(String lastName) {
		return null;
	}

	public Owner getOwner(Long id) {
		return new Owner(id);
	}

	public Long saveOwner(Owner owner) {
		return 1L;
	}

}
