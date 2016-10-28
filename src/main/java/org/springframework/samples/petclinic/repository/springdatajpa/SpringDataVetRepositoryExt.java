package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.repository.VetRepositoryExt;

@Qualifier("VetRepositoryExt")
public interface SpringDataVetRepositoryExt extends VetRepositoryExt, Repository<Vet, Integer> {

}
