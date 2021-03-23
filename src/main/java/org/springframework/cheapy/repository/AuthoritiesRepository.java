package org.springframework.cheapy.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.cheapy.model.Authorities;
import org.springframework.cheapy.model.User;


public interface AuthoritiesRepository extends  CrudRepository<Authorities, Integer>{
	
}
