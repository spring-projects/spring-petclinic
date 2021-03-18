package org.springframework.cheapy.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cheapy.model.Owner;
import org.springframework.cheapy.repository.OwnerRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class OwnerService {
	private OwnerRepository ownerRepository;


	@Autowired
	public OwnerService(final OwnerRepository ownerRepository) {
		this.ownerRepository = ownerRepository;
	}

	public Owner findOwnerById(final int id) {
		return this.ownerRepository.findById(id);
	}

	public Collection<Owner> findByLastName(final String lastname) { //
		return this.ownerRepository.findByLastName(lastname);

	}
	
	public void saveOwner(final Owner owner) throws DataAccessException { //
		this.ownerRepository.save(owner);

	}
}
