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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.common.*;
import org.springframework.samples.petclinic.dto.business.OwnerDTO;
import org.springframework.samples.petclinic.dto.business.PetDTO;
import org.springframework.samples.petclinic.dto.business.PetTypeDTO;
import org.springframework.samples.petclinic.service.business.OwnerService;
import org.springframework.samples.petclinic.service.business.PetService;
import org.springframework.samples.petclinic.service.business.PetTypeService;
import org.springframework.samples.petclinic.validator.PetDTOValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
@Controller
@RequestMapping("/owners/{ownerId}")
class PetController extends WebSocketSender {

	private final OwnerService ownerService;

	private final PetService petService;

	@Autowired
	PetController(OwnerService ownerService, PetService petService, PetTypeService petTypeService) {
		this.ownerService = ownerService;
		this.petService = petService;
	}

	@ModelAttribute("types")
	public Collection<PetTypeDTO> populatePetTypes() {
		return this.petService.findPetTypes();
	}

	@ModelAttribute("owner")
	public OwnerDTO findOwner(@PathVariable("ownerId") int ownerId) {
		return ownerService.findById(ownerId);
	}

	@InitBinder("owner")
	public void initOwnerBinder(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields(CommonAttribute.ID);
	}

	@InitBinder("pet")
	public void initPetBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new PetDTOValidator());
	}

	@GetMapping(CommonEndPoint.PETS_NEW)
	public String initCreationForm(@ModelAttribute("owner") OwnerDTO owner, ModelMap model) {
		PetDTO pet = new PetDTO();
		owner.addPet(pet);
		model.put(CommonAttribute.PET, pet);
		return CommonView.PET_CREATE_OR_UPDATE;
	}

	@PostMapping(CommonEndPoint.PETS_NEW)
	public String processCreationForm(@ModelAttribute("owner") OwnerDTO owner, @ModelAttribute("pet") @Valid PetDTO pet,
			BindingResult result, ModelMap model) {
		if (owner == null) {
			sendErrorMessage(CommonWebSocket.PET_CREATION_ERROR);
			result.rejectValue(CommonAttribute.OWNER, CommonError.NOT_FOUND_ARGS, CommonError.NOT_FOUND_MESSAGE);
		}
		else {
			if (StringUtils.hasLength(pet.getName()) && pet.isNew() && owner.getPet(pet.getName(), true) != null) {
				sendErrorMessage(CommonWebSocket.PET_CREATION_ERROR);
				result.rejectValue(CommonAttribute.NAME, CommonError.DUPLICATE_ARGS, CommonError.DUPLICATE_MESSAGE);
			}
			owner.addPet(pet);
		}

		if (result.hasErrors()) {
			sendErrorMessage(CommonWebSocket.PET_CREATION_ERROR);
			model.put(CommonAttribute.PET, pet);
			return CommonView.PET_CREATE_OR_UPDATE;
		}
		else {
			this.petService.save(pet);
			sendSuccessMessage(CommonWebSocket.PET_CREATED);
			return CommonView.OWNER_OWNERS_ID_R;
		}
	}

	@GetMapping("/pets/{petId}/edit")
	public String initUpdateForm(@PathVariable("petId") int petId, ModelMap model) {
		PetDTO pet = this.petService.findById(petId);
		model.put(CommonAttribute.PET, pet);
		return CommonView.PET_CREATE_OR_UPDATE;
	}

	@PostMapping("/pets/{petId}/edit")
	public String processUpdateForm(@ModelAttribute("pet") @Valid PetDTO pet, BindingResult result,
			@ModelAttribute("owner") OwnerDTO owner, ModelMap model) {
		if (result.hasErrors()) {
			pet.setOwner(owner);
			model.put(CommonAttribute.PET, pet);
			sendErrorMessage(CommonWebSocket.PET_UPDATED_ERROR);
			return CommonView.PET_CREATE_OR_UPDATE;
		}
		else {
			owner.addPet(pet);
			this.petService.save(pet);
			sendSuccessMessage(CommonWebSocket.PET_UPDATED);
			return CommonView.OWNER_OWNERS_ID_R;
		}
	}

}
