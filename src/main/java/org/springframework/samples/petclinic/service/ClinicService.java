package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.Owner;
import org.springframework.samples.petclinic.Pet;
import org.springframework.samples.petclinic.PetType;
import org.springframework.samples.petclinic.Vet;
import org.springframework.samples.petclinic.Visit;


/**
 * The high-level PetClinic business interface.
 *
 * <p>This is basically a data access object.
 * PetClinic doesn't have a dedicated business facade.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 */
public interface ClinicService {
	
	public Collection<PetType> getPetTypes() throws DataAccessException;
	
	public Owner findOwnerById(int id) throws DataAccessException;
	
	public Pet findPetById(int id) throws DataAccessException;

	public void storePet(Pet pet) throws DataAccessException;

	public void deletePet(int id) throws DataAccessException;
	
	public void storeVisit(Visit visit) throws DataAccessException;
	
	public Collection<Vet> getVets() throws DataAccessException;

}
