package org.springframework.cheapy.repository;

import org.springframework.cheapy.model.Client;
import org.springframework.cheapy.model.Code;
import org.springframework.cheapy.model.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

public interface CodeRepository extends Repository<Code, Integer> {

	void save(Code code);

	@Query("SELECT code FROM Code code WHERE code.code =:code")
	@Transactional(readOnly = true)
	Code findCodeByCode(String code);
	
}
