package org.springframework.samples.petclinic.core.usecases.owner;

import lombok.RequiredArgsConstructor;

import org.springframework.samples.petclinic.core.domain.owner.Owner;
import org.springframework.samples.petclinic.core.gateways.owner.OwnerRepositoryGateway;
import org.springframework.samples.petclinic.core.usecases.owner.ports.SaveOwnerPort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SaveOwnerUseCase implements SaveOwnerPort {

	private final OwnerRepositoryGateway ownerRepositoryGateway;

	@Override
	public Owner save(Owner owner) {
		return ownerRepositoryGateway.save(owner);
	}

}
