package org.springframework.samples.petclinic.owner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class OwnerServiceMap implements OwnerService {
	private final OwnerRepository ownerRepository;

	@Autowired
	public OwnerServiceMap(OwnerRepository ownerRepository) {
		this.ownerRepository = ownerRepository;
	}

	@Override
	public Owner findOwnerById(Integer id) {
		return ownerRepository.findById(id).orElse(null); // Handle Optional with null or custom logic
	}

	@Override
	public Set<Owner> getAllOwners() {
		return StreamSupport.stream(ownerRepository.findAll().spliterator(), false)
			.collect(Collectors.toSet());
	}

	@Override
	public Owner saveOwner(Owner owner) {
		return ownerRepository.save(owner);
	}

	@Override
	public void deleteOwner(Owner owner) {
		ownerRepository.delete(owner);
	}

	@Override
	public void deleteOwnerById(Integer id) {
		ownerRepository.deleteById(id);
	}
}
