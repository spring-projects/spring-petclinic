package org.springframework.samples.petclinic.core.usecases.owner.ports;

import org.springframework.samples.petclinic.core.domain.owner.Owner;

public interface SaveOwnerPort {

	Owner save(Owner owner);

}
