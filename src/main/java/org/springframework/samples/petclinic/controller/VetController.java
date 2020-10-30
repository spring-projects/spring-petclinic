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
import org.springframework.samples.petclinic.common.CommonView;
import org.springframework.samples.petclinic.dto.VetsDTO;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
@Controller
class VetController {

	private final VetService vetService;

	VetController(VetService vetService) {
		this.vetService = vetService;
	}

	@GetMapping(CommonEndPoint.VETS_HTML)
	public String showVetList(Map<String, Object> model) {
		// Here we are returning an object of type 'Vets' rather than a collection of Vet
		// objects so it is simpler for Object-Xml mapping
		VetsDTO vets = new VetsDTO();
		vets.getVetList().addAll(this.vetService.findAll());
		model.put(CommonAttribute.VETS, vets);
		return CommonView.VET_VETS_LIST;
	}

	@GetMapping(CommonEndPoint.VETS)
	public @ResponseBody VetsDTO showResourcesVetList() {
		// Here we are returning an object of type 'Vets' rather than a collection of Vet
		// objects so it is simpler for JSon/Object mapping
		VetsDTO vets = new VetsDTO();
		vets.getVetList().addAll(this.vetService.findAll());
		return vets;
	}

}
