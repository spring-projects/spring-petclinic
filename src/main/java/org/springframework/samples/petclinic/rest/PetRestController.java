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
package org.springframework.samples.petclinic.rest;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lim.han
 */
@RestController
@RequestMapping("/api")
public class PetRestController {

    private final ClinicService clinicService;


    @Autowired
    public PetRestController(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    @RequestMapping(value = "/pets/types", method = RequestMethod.GET)
    public Collection<PetType> populatePetTypes() {
        return this.clinicService.findPetTypes();
    }
    
    @RequestMapping(value = "/owners/{ownerId}/pets", method = RequestMethod.POST)
    public Pet addPet(@PathVariable("ownerId") int ownerId, @RequestBody Pet pet) {
        Owner owner = this.clinicService.findOwnerById(ownerId);
        owner.addPet(pet);
        this.clinicService.savePet(pet);
        return pet;
    }

    @RequestMapping(value = "/pets", method = RequestMethod.GET)
    public Collection<Pet> findAllPets() {
    	return this.clinicService.findPets();	
    }

    @RequestMapping(value = "/pets/search", method = RequestMethod.GET)
    public Collection<Pet> search(@RequestParam String q) {
    	return this.clinicService.findPetByName(q);
    }
}
