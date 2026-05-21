package org.springframework.samples.petclinic.core.gateways.vet;

import java.util.Collection;

import org.springframework.samples.petclinic.core.domain.vet.Vet;
import org.springframework.samples.petclinic.core.usecases.vet.PagedResult;

public interface VetRepositoryGateway {

	Collection<Vet> findAll();

	PagedResult<Vet> findAll(int page, int pageSize);

}
