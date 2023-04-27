package org.springframework.samples.petclinic.domain;

import java.util.Collection;
import org.springframework.data.domain.Page;
import org.springframework.samples.petclinic.infrastructure.persistence.vet.VetEntity;

public interface VetRepository {

	Page<VetEntity> getVetPage(int page, int pageSize);

	Collection<VetEntity> getVets();

}
