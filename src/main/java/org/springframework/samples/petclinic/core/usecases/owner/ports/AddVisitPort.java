package org.springframework.samples.petclinic.core.usecases.owner.ports;

import org.springframework.samples.petclinic.core.domain.owner.Visit;

public interface AddVisitPort {

	void addVisit(int ownerId, int petId, Visit visit);

}
