package org.springframework.samples.petclinic.owner;

import java.util.Set;

public interface OwnerService {
	Owner findOwnerById(Integer id);
	Set<Owner> getAllOwners();
	Owner saveOwner(Owner owner);
	void deleteOwner(Owner owner);
	void deleteOwnerById(Integer id);
}
