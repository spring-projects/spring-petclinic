package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.repository.OwnerRepositoryExt;
import org.springframework.samples.petclinic.repository.PetRepositoryExt;
import org.springframework.samples.petclinic.repository.PetTypeRepositoryExt;
import org.springframework.samples.petclinic.repository.SpecialtyRepositoryExt;
import org.springframework.samples.petclinic.repository.VetRepositoryExt;
import org.springframework.samples.petclinic.repository.VisitRepositoryExt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClinicServiceExtImpl extends ClinicServiceImpl implements ClinicServiceExt {
		
	private SpecialtyRepositoryExt specialtyRepositoryExt;
	private PetTypeRepositoryExt petTypeRepositoryExt;
	private PetRepositoryExt petRepositoryExt;
	private VetRepositoryExt vetRepositoryExt;
	private OwnerRepositoryExt ownerRepositoryExt;
	private VisitRepositoryExt visitRepositoryExt;
	
	@Autowired
	public ClinicServiceExtImpl(
				@Qualifier("PetRepositoryExt") PetRepositoryExt petRepositoryExt, 
				@Qualifier("VetRepositoryExt") VetRepositoryExt vetRepositoryExt,
				@Qualifier("OwnerRepositoryExt") OwnerRepositoryExt ownerRepositoryExt,
				@Qualifier("VisitRepositoryExt") VisitRepositoryExt visitRepositoryExt,
				@Qualifier("SpecialtyRepositoryExt") SpecialtyRepositoryExt specialtyRepositoryExt,
				@Qualifier("PetTypeRepositoryExt") PetTypeRepositoryExt petTypeRepositoryExt) {
		super(petRepositoryExt, vetRepositoryExt, ownerRepositoryExt, visitRepositoryExt);
		this.specialtyRepositoryExt = specialtyRepositoryExt; 
		this.petTypeRepositoryExt = petTypeRepositoryExt;
		this.petRepositoryExt = petRepositoryExt;
		this.vetRepositoryExt = vetRepositoryExt;
		this.ownerRepositoryExt = ownerRepositoryExt;
		this.visitRepositoryExt = visitRepositoryExt;
	}

	@Override
	@Transactional(readOnly = true)
	public Collection<Pet> findAllPets() throws DataAccessException {
		return petRepositoryExt.findAll();
	}

	@Override
	@Transactional
	public void deletePet(Pet pet) throws DataAccessException {
		petRepositoryExt.delete(pet);
	}

	@Override
	@Transactional(readOnly = true)
	public Visit findVisitById(int visitId) throws DataAccessException {
		return visitRepositoryExt.findById(visitId);
	}

	@Override
	@Transactional(readOnly = true)
	public Collection<Visit> findAllVisits() throws DataAccessException {
		return visitRepositoryExt.findAll();
	}

	@Override
	@Transactional
	public void deleteVisit(Visit visit) throws DataAccessException {
		visitRepositoryExt.delete(visit);
	}

	@Override
	@Transactional(readOnly = true)
	public Vet findVetById(int id) throws DataAccessException {
		return vetRepositoryExt.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Collection<Vet> findAllVets() throws DataAccessException {
		return vetRepositoryExt.findAll();
	}

	@Override
	@Transactional
	public void saveVet(Vet vet) throws DataAccessException {
		vetRepositoryExt.save(vet);
	}

	@Override
	@Transactional
	public void deleteVet(Vet vet) throws DataAccessException {
		vetRepositoryExt.delete(vet);
	}

	@Override
	@Transactional(readOnly = true)
	public Collection<Owner> findAllOwners() throws DataAccessException {
		return ownerRepositoryExt.findAll();
	}

	@Override
	@Transactional
	public void deleteOwner(Owner owner) throws DataAccessException {
		ownerRepositoryExt.delete(owner);
	}

	@Override
	@Transactional(readOnly = true)
	public PetType findPetTypeById(int petTypeId) {
		return petTypeRepositoryExt.findById(petTypeId);
	}

	@Override
	@Transactional(readOnly = true)
	public Collection<PetType> findAllPetTypes() throws DataAccessException {
		return petTypeRepositoryExt.findAll();
	}

	@Override
	@Transactional
	public void savePetType(PetType petType) throws DataAccessException {
		petTypeRepositoryExt.save(petType);
	}

	@Override
	@Transactional
	public void deletePetType(PetType petType) throws DataAccessException {
		petTypeRepositoryExt.delete(petType);
	}

	@Override
	@Transactional(readOnly = true)
	public Specialty findSpecialtyById(int specialtyId) {
		return specialtyRepositoryExt.findById(specialtyId);
	}

	@Override
	@Transactional(readOnly = true)
	public Collection<Specialty> findAllSpecialties() throws DataAccessException {
		return specialtyRepositoryExt.findAll();
	}

	@Override
	@Transactional
	public void saveSpecialty(Specialty specialty) throws DataAccessException {
		specialtyRepositoryExt.save(specialty);
	}

	@Override
	@Transactional
	public void deleteSpecialty(Specialty specialty) throws DataAccessException {
		specialtyRepositoryExt.delete(specialty);
	}

}
