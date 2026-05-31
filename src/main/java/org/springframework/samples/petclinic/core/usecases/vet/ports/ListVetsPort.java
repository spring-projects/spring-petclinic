package org.springframework.samples.petclinic.core.usecases.vet.ports;

import java.util.Collection;

import org.springframework.samples.petclinic.core.domain.vet.Vet;
import org.springframework.samples.petclinic.core.usecases.vet.PagedResult;

public interface ListVetsPort {

	Collection<Vet> listAll();

	PagedResult<Vet> listPaginated(int page, int pageSize);

}
