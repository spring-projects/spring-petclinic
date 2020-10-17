/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.controller;

import org.springframework.samples.petclinic.common.CommonAttribute;
import org.springframework.samples.petclinic.common.CommonEndPoint;
import org.springframework.samples.petclinic.common.CommonError;
import org.springframework.samples.petclinic.common.CommonView;
import org.springframework.samples.petclinic.dto.*;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.VisitService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Map;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
@Controller
class OwnerController {

	private final OwnerService ownerService;
	private final VisitService visitService;

	OwnerController(OwnerService ownerService, VisitService visitService) {
		this.ownerService = ownerService;
		this.visitService = visitService;
	}

	@InitBinder("owner")
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields(CommonAttribute.OWNER_ID);
	}

	@GetMapping(CommonEndPoint.OWNERS_NEW)
	public String initCreationForm(Map<String, Object> model) {
		OwnerDTO owner = new OwnerDTO();
		model.put(CommonAttribute.OWNER, owner);
		return CommonView.OWNER_CREATE_OR_UPDATE;
	}

	@PostMapping(CommonEndPoint.OWNERS_NEW)
	public String processCreationForm(@ModelAttribute(CommonAttribute.OWNER) @Valid OwnerDTO owner,
			BindingResult result) {
		if (result.hasErrors()) {
			return CommonView.OWNER_CREATE_OR_UPDATE;
		}
		else {
			this.ownerService.save(owner);
			return CommonView.OWNER_OWNERS_R + owner.getId();
		}
	}

	@GetMapping(CommonEndPoint.OWNERS_FIND)
	public String initFindForm(Map<String, Object> model) {
		model.put(CommonAttribute.OWNER, new OwnerDTO());
		return CommonView.OWNER_FIND_OWNERS;
	}

	@GetMapping(CommonEndPoint.OWNERS)
	public String processFindForm(OwnerDTO owner, BindingResult result,
			Map<String, Object> model) {

		// allow parameterless GET request for /owners to return all records
		if (owner.getLastName() == null) {
			owner.setLastName(""); // empty string signifies broadest possible search
		}

		// find owners by last name
		Collection<OwnerDTO> results = this.ownerService.findByLastName(owner.getLastName());
		if (results.isEmpty()) {
			// no owners found
			result.rejectValue(CommonAttribute.OWNER_LAST_NAME, CommonError.NOT_FOUND_ARGS,
					CommonError.NOT_FOUND_MESSAGE);
			return CommonView.OWNER_FIND_OWNERS;
		}
		else if (results.size() == 1) {
			// 1 owner found
			owner = results.iterator().next();
			return CommonView.OWNER_OWNERS_R + owner.getId();
		}
		else {
			// multiple owners found
			model.put(CommonAttribute.SELECTIONS, results);
			return CommonView.OWNER_OWNERS_LIST;
		}
	}

	@GetMapping(CommonEndPoint.OWNERS_ID_EDIT)
	public String initUpdateOwnerForm(@PathVariable("ownerId") int ownerId, Model model) {
		OwnerDTO ownerDTO = this.ownerService.findById(ownerId);
		model.addAttribute(CommonAttribute.OWNER, ownerDTO);
		return CommonView.OWNER_CREATE_OR_UPDATE;
	}

	@PostMapping(CommonEndPoint.OWNERS_ID_EDIT)
	public String processUpdateOwnerForm(@ModelAttribute(CommonAttribute.OWNER) @Valid OwnerDTO owner,
			BindingResult result, @PathVariable("ownerId") int ownerId) {
		if (result.hasErrors()) {
			return CommonView.OWNER_CREATE_OR_UPDATE;
		}
		else {
			owner.setId(ownerId);
			this.ownerService.save(owner);
			return CommonView.OWNER_OWNERS_ID_R;
		}
	}

	/**
	 * Custom handler for displaying an owner.
	 * @param ownerId the ID of the owner to display
	 * @return a ModelMap with the model attributes for the view
	 */
	@GetMapping(CommonEndPoint.OWNERS_ID)
	public ModelAndView showOwner(@PathVariable("ownerId") int ownerId) {
		ModelAndView modelAndView = new ModelAndView(CommonView.OWNER_DETAILS);
		OwnerDTO owner = this.ownerService.findById(ownerId);

		for (PetDTO petDTO : owner.getPets()) {
			petDTO.setVisitsInternal(visitService.findByPetId(petDTO.getId()));
		}

		modelAndView.addObject(CommonAttribute.OWNER, owner);
		return modelAndView;
	}

}
