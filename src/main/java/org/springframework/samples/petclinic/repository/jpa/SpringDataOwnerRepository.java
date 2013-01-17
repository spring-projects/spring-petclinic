package org.springframework.samples.petclinic.repository.jpa;

import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.Owner;
import org.springframework.samples.petclinic.repository.OwnerRepository;

/**
 *
 * @author Michael Isvy
 * @since 15.1.2013
 */
public interface SpringDataOwnerRepository extends OwnerRepository, Repository<Owner, Integer> {
}
