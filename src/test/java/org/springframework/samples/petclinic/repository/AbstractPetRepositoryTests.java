/*
 * Copyright 2002-2013 the original author or authors.
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
package org.springframework.samples.petclinic.repository;

import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * <p> Base class for {@link OwnerRepository} integration tests. </p>
 * <p/>
 * see javadoc inside {@link AbstractOwnerRepositoryTests} for more details
 *
 * @author Ken Krebs
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 */
public abstract class AbstractPetRepositoryTests {

	@Autowired
    protected ClinicService clinicService;


    @Test
    public void getPetTypes() {
        Collection<PetType> petTypes = this.clinicService.findPetTypes();

        PetType petType1 = EntityUtils.getById(petTypes, PetType.class, 1);
        assertEquals("cat", petType1.getName());
        PetType petType4 = EntityUtils.getById(petTypes, PetType.class, 4);
        assertEquals("snake", petType4.getName());
    }

    @Test
    public void findPet() {
        Collection<PetType> types = this.clinicService.findPetTypes();
        Pet pet7 = this.clinicService.findPetById(7);
        assertTrue(pet7.getName().startsWith("Samantha"));
        assertEquals(EntityUtils.getById(types, PetType.class, 1).getId(), pet7.getType().getId());
        assertEquals("Jean", pet7.getOwner().getFirstName());
        Pet pet6 = this.clinicService.findPetById(6);
        assertEquals("George", pet6.getName());
        assertEquals(EntityUtils.getById(types, PetType.class, 4).getId(), pet6.getType().getId());
        assertEquals("Peter", pet6.getOwner().getFirstName());
    }

    @Test
    @Transactional
    public void insertPet() {
        Owner owner6 = this.clinicService.findOwnerById(6);
        int found = owner6.getPets().size();
        Pet pet = new Pet();
        pet.setName("bowser");
        Collection<PetType> types = this.clinicService.findPetTypes();
        pet.setType(EntityUtils.getById(types, PetType.class, 2));
        pet.setBirthDate(new DateTime());
        owner6.addPet(pet);
        assertEquals(found + 1, owner6.getPets().size());
        // both storePet and storeOwner are necessary to cover all ORM tools
        this.clinicService.savePet(pet);
        this.clinicService.saveOwner(owner6);
        owner6 = this.clinicService.findOwnerById(6);
        assertEquals(found + 1, owner6.getPets().size());
    }

    @Test
    @Transactional
    public void updatePet() throws Exception {
        Pet pet7 = this.clinicService.findPetById(7);
        String old = pet7.getName();
        pet7.setName(old + "X");
        this.clinicService.savePet(pet7);
        pet7 = this.clinicService.findPetById(7);
        assertEquals(old + "X", pet7.getName());
    }

}
