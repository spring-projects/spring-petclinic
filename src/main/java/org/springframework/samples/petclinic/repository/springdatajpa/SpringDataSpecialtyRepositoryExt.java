package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.repository.SpecialtyRepositoryExt;

@Qualifier("SpecialtyRepositoryExt")
public interface SpringDataSpecialtyRepositoryExt extends SpecialtyRepositoryExt, Repository<Specialty, Integer> {

}
