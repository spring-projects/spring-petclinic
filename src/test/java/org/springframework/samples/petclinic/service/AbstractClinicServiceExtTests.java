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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.test.context.ContextConfiguration;

/**
 * <p> Base class for {@link ClinicServiceExt} integration tests. </p> <p> Subclasses should specify Spring context
 * configuration using {@link ContextConfiguration @ContextConfiguration} annotation </p> <p>
 * AbstractclinicServiceExtTests and its subclasses benefit from the following services provided by the Spring
 * TestContext Framework: </p> <ul> <li><strong>Spring IoC container caching</strong> which spares us unnecessary set up
 * time between test execution.</li> <li><strong>Dependency Injection</strong> of test fixture instances, meaning that
 * we don't need to perform application context lookups. See the use of {@link Autowired @Autowired} on the <code>{@link
 * AbstractClinicServiceExtTests#clinicServiceExt clinicServiceExt}</code> instance variable, which uses autowiring <em>by
 * type</em>. <li><strong>Transaction management</strong>, meaning each test method is executed in its own transaction,
 * which is automatically rolled back by default. Thus, even if tests insert or otherwise change database state, there
 * is no need for a teardown or cleanup script. <li> An {@link org.springframework.context.ApplicationContext
 * ApplicationContext} is also inherited and can be used for explicit bean lookup if necessary. </li> </ul>
 * 
 * @author Vitaliy Fedoriv
 *
 */

public abstract class AbstractClinicServiceExtTests {
	
    @Autowired
    @Qualifier("ClinicServiceExt")
    protected ClinicServiceExt clinicService;
    
    @Test
    public void shouldFindAllPets(){
        Collection<Pet> pets = this.clinicService.findAllPets();
        Pet pet1 = EntityUtils.getById(pets, Pet.class, 1);
        assertThat(pet1.getName()).isEqualTo("Leo");
        Pet pet3 = EntityUtils.getById(pets, Pet.class, 3);
        assertThat(pet3.getName()).isEqualTo("Rosy");
    }
    
    @Test
    @Transactional
    public void shouldDeletePet(){
        Pet pet = this.clinicService.findPetById(1);
        this.clinicService.deletePet(pet);
        try {
        pet = this.clinicService.findPetById(1);
		} catch (Exception e) {
			pet = null;
		}
        assertThat(pet).isNull();
    }
    
    @Test
    public void shouldFindVisitDyId(){
    	Visit visit = this.clinicService.findVisitById(1);
    	assertThat(visit.getId()).isEqualTo(1);
    	assertThat(visit.getPet().getName()).isEqualTo("Samantha");
    }
    
    @Test
    public void shouldFindAllVisits(){
        Collection<Visit> visits = this.clinicService.findAllVisits();
        Visit visit1 = EntityUtils.getById(visits, Visit.class, 1);
        assertThat(visit1.getPet().getName()).isEqualTo("Samantha");
        Visit visit3 = EntityUtils.getById(visits, Visit.class, 3);
        assertThat(visit3.getPet().getName()).isEqualTo("Max");
    }
    
    @Test
    @Transactional
    public void shouldInsertVisit() {
        Collection<Visit> visits = this.clinicService.findAllVisits();
        int found = visits.size();
        
        Pet pet = this.clinicService.findPetById(1);

        Visit visit = new Visit();
        visit.setPet(pet);
        visit.setDate(new Date());
        visit.setDescription("new visit");
        
                
        this.clinicService.saveVisit(visit);
        assertThat(visit.getId().longValue()).isNotEqualTo(0);

        visits = this.clinicService.findAllVisits();
        assertThat(visits.size()).isEqualTo(found + 1);
    }
    
    @Test
    @Transactional
    public void shouldUpdateVisit(){
    	Visit visit = this.clinicService.findVisitById(1);
    	String oldDesc = visit.getDescription();
        String newDesc = oldDesc + "X";
        visit.setDescription(newDesc);
        this.clinicService.saveVisit(visit);
        visit = this.clinicService.findVisitById(1);
        assertThat(visit.getDescription()).isEqualTo(newDesc);
    }
    
    @Test
    @Transactional
    public void shouldDeleteVisit(){
    	Visit visit = this.clinicService.findVisitById(1);
        this.clinicService.deleteVisit(visit);
        try {
        	visit = this.clinicService.findVisitById(1);
		} catch (Exception e) {
			visit = null;
		}
        assertThat(visit).isNull();
    }
    
    @Test
    public void shouldFindVetDyId(){
    	Vet vet = this.clinicService.findVetById(1);
    	assertThat(vet.getFirstName()).isEqualTo("James");
    	assertThat(vet.getLastName()).isEqualTo("Carter");
    }
    
    @Test
    @Transactional
    public void shouldInsertVet() {
        Collection<Vet> vets = this.clinicService.findAllVets();
        int found = vets.size();

        Vet vet = new Vet();
        vet.setFirstName("John");
        vet.setLastName("Dow");
                
        this.clinicService.saveVet(vet);
        assertThat(vet.getId().longValue()).isNotEqualTo(0);

        vets = this.clinicService.findAllVets();
        assertThat(vets.size()).isEqualTo(found + 1);
    }
    
    @Test
    @Transactional
    public void shouldUpdateVet(){
    	Vet vet = this.clinicService.findVetById(1);
    	String oldLastName = vet.getLastName();
        String newLastName = oldLastName + "X";
        vet.setLastName(newLastName);
        this.clinicService.saveVet(vet);
        vet = this.clinicService.findVetById(1);
        assertThat(vet.getLastName()).isEqualTo(newLastName);
    }
    
    @Test
    @Transactional
    public void shouldDeleteVet(){
    	Vet vet = this.clinicService.findVetById(1);
        this.clinicService.deleteVet(vet);
        try {
        	vet = this.clinicService.findVetById(1);
		} catch (Exception e) {
			vet = null;
		}
        assertThat(vet).isNull();
    }
    
    @Test
    public void shouldFindAllOwners(){
        Collection<Owner> owners = this.clinicService.findAllOwners();
        Owner owner1 = EntityUtils.getById(owners, Owner.class, 1);
        assertThat(owner1.getFirstName()).isEqualTo("George");
        Owner owner3 = EntityUtils.getById(owners, Owner.class, 3);
        assertThat(owner3.getFirstName()).isEqualTo("Eduardo");
    }
    
    @Test
    @Transactional
    public void shouldDeleteOwner(){
    	Owner owner = this.clinicService.findOwnerById(1);
        this.clinicService.deleteOwner(owner);
        try {
        	owner = this.clinicService.findOwnerById(1);
		} catch (Exception e) {
			owner = null;
		}
        assertThat(owner).isNull();
    }
    
    @Test
    public void shouldFindPetTypeById(){
    	PetType petType = this.clinicService.findPetTypeById(1);
    	assertThat(petType.getName()).isEqualTo("cat");
    }
    
    @Test
    public void shouldFindAllPetTypes(){
        Collection<PetType> petTypes = this.clinicService.findAllPetTypes();
        PetType petType1 = EntityUtils.getById(petTypes, PetType.class, 1);
        assertThat(petType1.getName()).isEqualTo("cat");
        PetType petType3 = EntityUtils.getById(petTypes, PetType.class, 3);
        assertThat(petType3.getName()).isEqualTo("lizard");
    }
    
    @Test
    @Transactional
    public void shouldInsertPetType() {
        Collection<PetType> petTypes = this.clinicService.findAllPetTypes();
        int found = petTypes.size();

        PetType petType = new PetType();
        petType.setName("tiger");
        
        this.clinicService.savePetType(petType);
        assertThat(petType.getId().longValue()).isNotEqualTo(0);

        petTypes = this.clinicService.findAllPetTypes();
        assertThat(petTypes.size()).isEqualTo(found + 1);
    }
    
    @Test
    @Transactional
    public void shouldUpdatePetType(){
    	PetType petType = this.clinicService.findPetTypeById(1);
    	String oldLastName = petType.getName();
        String newLastName = oldLastName + "X";
        petType.setName(newLastName);
        this.clinicService.savePetType(petType);
        petType = this.clinicService.findPetTypeById(1);
        assertThat(petType.getName()).isEqualTo(newLastName);
    }
    
    @Test
    @Transactional
    public void shouldDeletePetType(){
    	PetType petType = this.clinicService.findPetTypeById(1);
        this.clinicService.deletePetType(petType);
        try {
        	petType = this.clinicService.findPetTypeById(1);
		} catch (Exception e) {
			petType = null;
		}
        assertThat(petType).isNull();
    }
    
    @Test
    public void shouldFindSpecialtyById(){
    	Specialty specialty = this.clinicService.findSpecialtyById(1);
    	assertThat(specialty.getName()).isEqualTo("radiology");
    }
    
    @Test
    public void shouldFindAllSpecialtys(){
        Collection<Specialty> specialties = this.clinicService.findAllSpecialties();
        Specialty specialty1 = EntityUtils.getById(specialties, Specialty.class, 1);
        assertThat(specialty1.getName()).isEqualTo("radiology");
        Specialty specialty3 = EntityUtils.getById(specialties, Specialty.class, 3);
        assertThat(specialty3.getName()).isEqualTo("dentistry");
    }
    
    @Test
    @Transactional
    public void shouldInsertSpecialty() {
        Collection<Specialty> specialties = this.clinicService.findAllSpecialties();
        int found = specialties.size();

        Specialty specialty = new Specialty();
        specialty.setName("dermatologist");
        
        this.clinicService.saveSpecialty(specialty);
        assertThat(specialty.getId().longValue()).isNotEqualTo(0);

        specialties = this.clinicService.findAllSpecialties();
        assertThat(specialties.size()).isEqualTo(found + 1);
    }
    
    @Test
    @Transactional
    public void shouldUpdateSpecialty(){
    	Specialty specialty = this.clinicService.findSpecialtyById(1);
    	String oldLastName = specialty.getName();
        String newLastName = oldLastName + "X";
        specialty.setName(newLastName);
        this.clinicService.saveSpecialty(specialty);
        specialty = this.clinicService.findSpecialtyById(1);
        assertThat(specialty.getName()).isEqualTo(newLastName);
    }
    
    @Test
    @Transactional
    public void shouldDeleteSpecialty(){
    	Specialty specialty = this.clinicService.findSpecialtyById(1);
        this.clinicService.deleteSpecialty(specialty);
        try {
        	specialty = this.clinicService.findSpecialtyById(1);
		} catch (Exception e) {
			specialty = null;
		}
        assertThat(specialty).isNull();
    }
       


}
