package org.springframework.samples.petclinic.core.usecases.owner;

import lombok.RequiredArgsConstructor;

import org.springframework.samples.petclinic.core.domain.owner.Owner;
import org.springframework.samples.petclinic.core.domain.owner.Visit;
import org.springframework.samples.petclinic.core.gateways.owner.OwnerRepositoryGateway;
import org.springframework.samples.petclinic.core.usecases.owner.ports.AddVisitPort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddVisitUseCase implements AddVisitPort {

	private final OwnerRepositoryGateway ownerRepositoryGateway;

	@Override
	public void addVisit(int ownerId, int petId, Visit visit) {
		Owner owner = ownerRepositoryGateway.findById(ownerId)
			.orElseThrow(() -> new IllegalArgumentException("Owner not found with id: " + ownerId));
		if (owner.getPet(petId) == null) {
			throw new IllegalArgumentException("Pet not found with id: " + petId);
		}
		owner.addVisit(petId, visit);
		ownerRepositoryGateway.save(owner);
	}

}
