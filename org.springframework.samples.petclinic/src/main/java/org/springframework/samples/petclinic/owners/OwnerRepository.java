package org.springframework.samples.petclinic.owners;

import java.util.Collection;

public interface OwnerRepository {

	Collection<Owner> findOwnersByLastName(String lastName);

	Owner getOwner(Long id);

	Long saveOwner(Owner owner);

}