/*
 * Copyright 2002-2014 the original author or authors.
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

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lim.han
 */
@RestController
@RequestMapping("/api")
public class OwnerRestController {

    private final ClinicService clinicService;

    @Autowired
    public OwnerRestController(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    @RequestMapping(value = "/owners", method = RequestMethod.POST)
    public @ResponseBody Owner create(@RequestBody Owner owner) {
    	if (owner.getId()!=null && owner.getId()>0){
    		Owner existingOwner = clinicService.findOwnerById(owner.getId());
    		BeanUtils.copyProperties(owner, existingOwner, "pets", "id");
    		clinicService.saveOwner(existingOwner);
    	}
    	else {
    		this.clinicService.saveOwner(owner);
    	}
    	
    	return owner;
    }
    
    @RequestMapping(value = "/owners", method = RequestMethod.PUT)
    public @ResponseBody Collection<Owner> create(@RequestBody Collection<Owner> owners) {
    	for(Owner owner : owners) {
    		this.clinicService.saveOwner(owner);
    	}
    	
    	return owners;
    }

    @RequestMapping(value = "/owners/{id}", method = RequestMethod.GET)
    public @ResponseBody Owner find(@PathVariable Integer id) {
        return this.clinicService.findOwnerById(id);
    }

    @RequestMapping(value = "/owners", method = RequestMethod.GET)
    public @ResponseBody Collection<Owner> findByLastName(@RequestParam(defaultValue="") String lastName) {
        return this.clinicService.findOwnerByLastName(lastName);
    }
}
