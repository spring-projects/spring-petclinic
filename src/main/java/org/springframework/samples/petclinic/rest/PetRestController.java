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

package org.springframework.samples.petclinic.rest;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.service.ClinicServiceExt;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author Vitaliy Fedoriv
 *
 */

@RestController
@RequestMapping("api/pets")
public class PetRestController {
	
	@Autowired
	private ClinicServiceExt clinicService;
	
	@RequestMapping(value = "/{petId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Pet> getPet(@PathVariable("petId") int petId){
		Pet pet = this.clinicService.findPetById(petId);
		if(pet == null){
			return new ResponseEntity<Pet>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Pet>(pet, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/pettypes}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Collection<PetType>> getPetTypes(){
		return new ResponseEntity<Collection<PetType>>(this.clinicService.findPetTypes(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Void> addPet(@RequestBody @Valid Pet pet, BindingResult bindingResult, UriComponentsBuilder ucBuilder){
		if(bindingResult.hasErrors() || (pet == null)){
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}
		this.clinicService.savePet(pet);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/pets/{id}").buildAndExpand(pet.getId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{petId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Pet> updatePet(@PathVariable("petId") int petId, @RequestBody @Valid Pet pet, BindingResult bindingResult){
		if(bindingResult.hasErrors() || (pet == null)){
			return new ResponseEntity<Pet>(HttpStatus.BAD_REQUEST);
		}
		Pet currentPet = this.clinicService.findPetById(petId);
		if(currentPet == null){
			return new ResponseEntity<Pet>(HttpStatus.NOT_FOUND);
		}
		currentPet.setBirthDate(pet.getBirthDate());
		currentPet.setName(pet.getName());
		currentPet.setType(pet.getType());
		this.clinicService.savePet(currentPet);
		return new ResponseEntity<Pet>(currentPet, HttpStatus.NO_CONTENT);
	}
	
	@RequestMapping(value = "/{petId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Void> deletePet(@PathVariable("petId") int petId){
		Pet pet = this.clinicService.findPetById(petId);
		if(pet == null){
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
		this.clinicService.deletePet(pet);
		// TODO  delete error - FK etc.
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
	

}
