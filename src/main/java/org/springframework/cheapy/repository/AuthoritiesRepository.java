package org.springframework.cheapy.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cheapy.model.Authorities;
import org.springframework.dao.DataAccessException;

public interface AuthoritiesRepository extends  CrudRepository<Authorities, String>{

//	@Autowired
//	void save(Authorities authorities);
	
	
}
