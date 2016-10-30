package org.springframework.samples.petclinic.repository.jdbc;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.repository.SpecialtyRepositoryExt;
import org.springframework.stereotype.Repository;

@Repository
@Qualifier("SpecialtyRepositoryExt")
public class JdbcSpecialtyRepositoryExtImpl implements SpecialtyRepositoryExt {

	@Override
	public Specialty findById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Specialty> findAll() throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(Specialty specialty) throws DataAccessException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Specialty specialty) throws DataAccessException {
		// TODO Auto-generated method stub

	}

}
