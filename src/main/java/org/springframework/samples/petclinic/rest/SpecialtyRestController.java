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

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.model.Specialty;
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
@RequestMapping("api/specialties")
public class SpecialtyRestController {
	
	@Autowired
	private ClinicServiceExt clinicService;
	
	@RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Collection<Specialty>> getAllSpecialtys(){
		Collection<Specialty> specialties = new ArrayList<Specialty>();
		specialties.addAll(this.clinicService.findAllSpecialties());
		if (specialties.isEmpty()){
			return new ResponseEntity<Collection<Specialty>>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Collection<Specialty>>(specialties, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{specialtyId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Specialty> getSpecialty(@PathVariable("specialtyId") int specialtyId){
		Specialty specialty = this.clinicService.findSpecialtyById(specialtyId);
		if(specialty == null){
			return new ResponseEntity<Specialty>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Specialty>(specialty, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Void> addSpecialty(@RequestBody @Valid Specialty specialty, BindingResult bindingResult, UriComponentsBuilder ucBuilder){
		if(bindingResult.hasErrors() || (specialty == null)){
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}
		this.clinicService.saveSpecialty(specialty);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/specialtys/{id}").buildAndExpand(specialty.getId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{specialtyId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Specialty> updateSpecialty(@PathVariable("specialtyId") int specialtyId, @RequestBody @Valid Specialty specialty, BindingResult bindingResult){
		if(bindingResult.hasErrors() || (specialty == null)){
			return new ResponseEntity<Specialty>(HttpStatus.BAD_REQUEST);
		}
		Specialty currentSpecialty = this.clinicService.findSpecialtyById(specialtyId);
		if(currentSpecialty == null){
			return new ResponseEntity<Specialty>(HttpStatus.NOT_FOUND);
		}
		currentSpecialty.setName(specialty.getName());
		this.clinicService.saveSpecialty(currentSpecialty);
		return new ResponseEntity<Specialty>(currentSpecialty, HttpStatus.NO_CONTENT);
	}
	
	@RequestMapping(value = "/{specialtyId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@Transactional
	public ResponseEntity<Void> deleteSpecialty(@PathVariable("specialtyId") int specialtyId){
		Specialty specialty = this.clinicService.findSpecialtyById(specialtyId);
		if(specialty == null){
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
		this.clinicService.deleteSpecialty(specialty);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

}
