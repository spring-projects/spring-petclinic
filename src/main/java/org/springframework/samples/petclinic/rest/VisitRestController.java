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

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/api")
public class VisitRestController {

    private final ClinicService clinicService;

    @Autowired
    public VisitRestController(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

	// Spring MVC calls method loadPetWithVisit(...) before processNewVisitForm is called
    @RequestMapping(value = "/pets/{petId}/visits", method = RequestMethod.POST)
    public Visit createVisit(@PathVariable int ownerId, @PathVariable int petId, @Valid Visit visit) {
           this.clinicService.saveVisit(visit);
           return visit;
    }

    @RequestMapping(value = "/pets/{petId}/visits", method = RequestMethod.GET)
    public List<Visit> showVisits(@PathVariable int petId) {
        return this.clinicService.findPetById(petId).getVisits();
    }

}
