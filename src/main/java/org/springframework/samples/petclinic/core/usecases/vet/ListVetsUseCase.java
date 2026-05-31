package org.springframework.samples.petclinic.core.usecases.vet;

import java.util.Collection;

import lombok.RequiredArgsConstructor;

import org.springframework.samples.petclinic.core.domain.vet.Vet;
import org.springframework.samples.petclinic.core.gateways.vet.VetRepositoryGateway;
import org.springframework.samples.petclinic.core.usecases.vet.ports.ListVetsPort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ListVetsUseCase implements ListVetsPort {

	private final VetRepositoryGateway vetRepositoryGateway;

	@Override
	public Collection<Vet> listAll() {
		return vetRepositoryGateway.findAll();
	}

	@Override
	public PagedResult<Vet> listPaginated(int page, int pageSize) {
		return vetRepositoryGateway.findAll(page - 1, pageSize);
	}

}
