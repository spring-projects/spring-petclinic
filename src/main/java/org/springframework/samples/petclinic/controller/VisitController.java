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

import java.util.Map;

import javax.validation.Valid;

import org.springframework.samples.petclinic.common.CommonAttribute;
import org.springframework.samples.petclinic.common.CommonEndPoint;
import org.springframework.samples.petclinic.common.CommonView;
import org.springframework.samples.petclinic.common.CommonWebSocket;
import org.springframework.samples.petclinic.controller.common.WebSocketSender;
import org.springframework.samples.petclinic.dto.business.PetDTO;
import org.springframework.samples.petclinic.dto.business.VisitDTO;
import org.springframework.samples.petclinic.service.business.PetService;
import org.springframework.samples.petclinic.service.business.VisitService;
import org.springframework.samples.petclinic.validator.VisitDTOValidator;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 * @author Dave Syer
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
@Controller
class VisitController extends WebSocketSender {

	private final VisitService visitService;

	private final PetService petService;

	VisitController(VisitService visitService, PetService petService) {
		this.visitService = visitService;
		this.petService = petService;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	/**
	 * Called before each and every @RequestMapping annotated method. 2 goals: - Make sure
	 * we always have fresh data - Since we do not use the session scope, make sure that
	 * Pet object always has an id (Even though id is not part of the form fields)
	 * @param petId Pet identification
	 * @return Pet
	 */
	@ModelAttribute("visit")
	public VisitDTO loadPetWithVisit(@PathVariable("petId") int petId, Map<String, Object> model) {
		PetDTO pet = this.petService.findById(petId);
		pet.setVisitsInternal(this.visitService.findByPetId(petId));
		model.put(CommonAttribute.PET, pet);
		VisitDTO visit = new VisitDTO();
		pet.addVisit(visit);
		return visit;
	}

	@InitBinder("visit")
	public void initVisitBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new VisitDTOValidator());
	}

	// Spring MVC calls method loadPetWithVisit(...) before initNewVisitForm is called
	@GetMapping(CommonEndPoint.VISITS_NEW)
	public String initNewVisitForm(@PathVariable("petId") int petId, Map<String, Object> model) {
		return CommonView.VISIT_CREATE_OR_UPDATE;
	}

	// Spring MVC calls method loadPetWithVisit(...) before processNewVisitForm is called
	@PostMapping(CommonEndPoint.VISITS_EDIT)
	public String processNewVisitForm(@ModelAttribute(CommonAttribute.VISIT) @Valid VisitDTO visit,
			BindingResult result) {
		if (result.hasErrors()) {
			sendErrorMessage(CommonWebSocket.VISIT_CREATION_ERROR);
			return CommonView.VISIT_CREATE_OR_UPDATE;
		}
		else {
			this.visitService.save(visit);
			sendSuccessMessage(CommonWebSocket.VISIT_CREATED);
			return CommonView.OWNER_OWNERS_ID_R;
		}
	}

}
