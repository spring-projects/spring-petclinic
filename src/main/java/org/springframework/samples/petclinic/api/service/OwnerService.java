package org.springframework.samples.petclinic.api.service;

import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.stereotype.Service;

@Service
public class OwnerService {

	private OwnerRepository ownerRepository;

	public void addOwner(Owner owner) {
		ownerRepository.save(owner);
	}

}
