package org.springframework.cheapy.repository;

import org.springframework.data.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cheapy.model.Authorities;

public interface AuthoritiesRepository extends  Repository<Authorities, Integer>{

	@Autowired
	void save(Authorities authorities);
}
