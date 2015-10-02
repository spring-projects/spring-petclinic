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

import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@Controller
@SessionAttributes(types = Owner.class)
public class OwnerController {

    private final ClinicService clinicService;


    @Autowired
    public OwnerController(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @RequestMapping(value = "/owners/new", method = RequestMethod.GET)
    public String initCreationForm(Map<String, Object> model) {
        Owner owner = new Owner();
        model.put("owner", owner);
        return "owners/createOrUpdateOwnerForm";
    }

    @RequestMapping(value = "/owners/new", method = RequestMethod.POST)
    public String processCreationForm(@Valid Owner owner, BindingResult result, SessionStatus status) {
        if (result.hasErrors()) {
            return "owners/createOrUpdateOwnerForm";
        } else {
            this.clinicService.saveOwner(owner);
            status.setComplete();
            return "redirect:/owners/" + owner.getId();
        }
    }

    @RequestMapping(value = "/owners/find", method = RequestMethod.GET)
    public String initFindForm(Map<String, Object> model) {
        model.put("owner", new Owner());
        return "owners/findOwners";
    }

    @RequestMapping(value = "/owners", method = RequestMethod.GET)
    public String processFindForm(Owner owner, BindingResult result, Map<String, Object> model) {

        // allow parameterless GET request for /owners to return all records
        if (owner.getLastName() == null) {
            owner.setLastName(""); // empty string signifies broadest possible search
        }

        // find owners by last name
        Collection<Owner> results = this.clinicService.findOwnerByLastName(owner.getLastName());
        if (results.isEmpty()) {
            // no owners found
            result.rejectValue("lastName", "notFound", "not found");
            return "owners/findOwners";
        }
        else if (results.size() == 1) {
    	// 1 owner found
    	owner = results.iterator().next();
    	return "redirect:/owners/" + owner.getId();
        }
        else {
            // multiple owners found
            model.put("selections", results);
            return "owners/ownersList";
        }
    }
        
    @RequestMapping(value = "/owners/{ownerId}/edit", method = RequestMethod.GET)
    public String initUpdateOwnerForm(@PathVariable("ownerId") int ownerId, Model model) {
        Owner owner = this.clinicService.findOwnerById(ownerId);
        model.addAttribute(owner);
        return "owners/createOrUpdateOwnerForm";
    }

    @RequestMapping(value = "/owners/{ownerId}/edit", method = RequestMethod.POST)
    public String processUpdateOwnerForm(@Valid Owner owner, BindingResult result, SessionStatus status) {
        if (result.hasErrors()) {
            return "owners/createOrUpdateOwnerForm";
        } else {
            this.clinicService.saveOwner(owner);
            status.setComplete();
            return "redirect:/owners/{ownerId}";
        }
    }

    /**
     * Custom handler for displaying an owner.
     *
     * @param ownerId the ID of the owner to display
     * @return a ModelMap with the model attributes for the view
     */
    @RequestMapping("/owners/{ownerId}")
    public ModelAndView showOwner(@PathVariable("ownerId") int ownerId) {
        ModelAndView mav = new ModelAndView("owners/ownerDetails");
        mav.addObject(this.clinicService.findOwnerById(ownerId));
        return mav;
    }

}
