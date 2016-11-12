/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Visit;

/**
 * Used as a facade so all (incl.extended) controllers have a single point of entry
 *
 * @author Vitaliy Fedoriv
 */

public interface ClinicServiceExt extends ClinicService {

	//  ----- Pet -----
	// (in ClinicService)  Pet findPetById(int id) throws DataAccessException;
	Collection<Pet> findAllPets() throws DataAccessException;
	// (in ClinicService)  void savePet(Pet pet) throws DataAccessException;
	void deletePet(Pet pet) throws DataAccessException;

	//  ----- Visit -----
	// (in ClinicService)	Collection<Visit> findVisitsByPetId(int petId);
	Visit findVisitById(int visitId) throws DataAccessException;
	Collection<Visit> findAllVisits() throws DataAccessException;
	// (in ClinicService)  void saveVisit(Visit visit) throws DataAccessException;
	void deleteVisit(Visit visit) throws DataAccessException;
	
	//  ----- Vet -----
	Vet findVetById(int id) throws DataAccessException;
	// (in ClinicService)  Collection<Vet> findVets() throws DataAccessException;
	Collection<Vet> findAllVets() throws DataAccessException;
	void saveVet(Vet vet) throws DataAccessException;
	void deleteVet(Vet vet) throws DataAccessException;
	
	//  ----- Owner -----
	// (in ClinicService)  Owner findOwnerById(int id) throws DataAccessException;
	Collection<Owner> findAllOwners() throws DataAccessException;
	// (in ClinicService)  void saveOwner(Owner owner) throws DataAccessException;
	void deleteOwner(Owner owner) throws DataAccessException;
	// (in ClinicService)  Collection<Owner> findOwnerByLastName(String lastName) throws DataAccessException;

	//  ----- PetType -----
	PetType findPetTypeById(int petTypeId);
	Collection<PetType> findAllPetTypes() throws DataAccessException;
	// (in ClinicService)  Collection<PetType> findPetTypes() throws DataAccessException;
	void savePetType(PetType petType) throws DataAccessException;
	void deletePetType(PetType petType) throws DataAccessException;
	
	//  ----- Specialty -----
	Specialty findSpecialtyById(int specialtyId);
	Collection<Specialty> findAllSpecialties() throws DataAccessException;
	void saveSpecialty(Specialty specialty) throws DataAccessException;
	void deleteSpecialty(Specialty specialty) throws DataAccessException;

}
