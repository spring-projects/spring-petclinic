package org.springframework.samples.petclinic.repository.jdbc;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.repository.PetTypeRepositoryExt;
import org.springframework.stereotype.Repository;

@Repository
@Qualifier("PetTypeRepositoryExt")
public class JdbcPetTypeRepositoryExtImpl implements PetTypeRepositoryExt {

	@Override
	public PetType findById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<PetType> findAll() throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(PetType petType) throws DataAccessException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(PetType petType) throws DataAccessException {
		// TODO Auto-generated method stub

	}

}
