package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.repository.VisitRepositoryExt;

@Qualifier("VisitRepositoryExt")
public interface SpringDataVisitRepositoryExt extends VisitRepositoryExt, Repository<Visit, Integer> {

}
