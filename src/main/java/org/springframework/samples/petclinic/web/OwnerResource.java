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
package org.springframework.samples.petclinic.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@RestController
public class OwnerResource {

    private final ClinicService clinicService;


    @Autowired
    public OwnerResource(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }
    
    @ModelAttribute
    public Owner retrieveOwner(@PathVariable("ownerId") int ownerId) {
        return this.clinicService.findOwnerById(ownerId);
    }

    // TODO: should be improved so we have a single method parameter
    @RequestMapping(value = "/owner/{ownerId}", method = RequestMethod.PUT)
    public Owner updateOwner(@ModelAttribute Owner ownerModel, @RequestBody Owner ownerRequest) {
    	ownerModel.setFirstName(ownerRequest.getFirstName());
    	ownerModel.setLastName(ownerRequest.getLastName());
    	ownerModel.setCity(ownerRequest.getCity());
    	ownerModel.setAddress(ownerRequest.getAddress());
    	ownerModel.setTelephone(ownerRequest.getTelephone());
        this.clinicService.saveOwner(ownerModel);
        return ownerModel;
        // TODO: need to handle failure
    }


    @RequestMapping(value = "/owner/{ownerId}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Owner findOwner(Owner owner) {
    	// Has already been retrieved from 'retrieveOwner' method
        return owner;
    }

}
