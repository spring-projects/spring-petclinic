package org.springframework.samples.petclinic.core.gateways.owner;

import java.util.Optional;

import org.springframework.samples.petclinic.core.domain.owner.Owner;
import org.springframework.samples.petclinic.core.usecases.owner.PagedResult;

public interface OwnerRepositoryGateway {

	Optional<Owner> findById(Integer id);

	PagedResult<Owner> findByLastNameStartingWith(String lastName, int page, int pageSize);

	Owner save(Owner owner);

}
