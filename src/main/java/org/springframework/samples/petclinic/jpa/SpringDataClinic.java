package org.springframework.samples.petclinic.jpa;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.Clinic;
import org.springframework.samples.petclinic.Owner;
import org.springframework.samples.petclinic.Pet;
import org.springframework.samples.petclinic.PetType;
import org.springframework.samples.petclinic.Vet;
import org.springframework.samples.petclinic.Visit;

/**
 *
 * @author Michael Isvy
 * @since 15.1.2013
 */
public interface SpringDataClinic extends Clinic, Repository {



	@Query("SELECT vet FROM Vet vet ORDER BY vet.lastName, vet.firstName")
	public Collection<Vet> getVets();

	@Query("SELECT ptype FROM PetType ptype ORDER BY ptype.name")
	public Collection<PetType> getPetTypes();

	@Query("SELECT owner FROM Owner owner WHERE owner.lastName LIKE :lastName")
	public Collection<Owner> findOwners(String lastName);
	

	public Owner findOwner(int id);

	public Pet findPet(int id);

	public void storeOwner(Owner owner);

	public void storePet(Pet pet);

	public void storeVisit(Visit visit);

	public void deletePet(int id);

}
