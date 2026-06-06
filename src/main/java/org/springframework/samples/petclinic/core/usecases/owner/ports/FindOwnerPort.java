package org.springframework.samples.petclinic.core.usecases.owner.ports;

import java.util.Optional;

import org.springframework.samples.petclinic.core.domain.owner.Owner;
import org.springframework.samples.petclinic.core.usecases.owner.PagedResult;

public interface FindOwnerPort {

	Optional<Owner> findById(Integer id);

	PagedResult<Owner> findByLastName(String lastName, int page);

	Optional<Owner> findUniqueByLastName(String lastName);

}
