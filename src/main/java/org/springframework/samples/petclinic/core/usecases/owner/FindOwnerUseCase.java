package org.springframework.samples.petclinic.core.usecases.owner;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.springframework.samples.petclinic.core.domain.owner.Owner;
import org.springframework.samples.petclinic.core.gateways.owner.OwnerRepositoryGateway;
import org.springframework.samples.petclinic.core.usecases.owner.ports.FindOwnerPort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindOwnerUseCase implements FindOwnerPort {

	private static final int PAGE_SIZE = 5;

	private final OwnerRepositoryGateway ownerRepositoryGateway;

	@Override
	public Optional<Owner> findById(Integer id) {
		return ownerRepositoryGateway.findById(id);
	}

	@Override
	public PagedResult<Owner> findByLastName(String lastName, int page) {
		String search = (lastName == null) ? "" : lastName;
		return ownerRepositoryGateway.findByLastNameStartingWith(search, page - 1, PAGE_SIZE);
	}

}
