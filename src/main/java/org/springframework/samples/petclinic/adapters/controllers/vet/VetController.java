/*
 * Copyright 2012-2025 the original author or authors.
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
package org.springframework.samples.petclinic.adapters.controllers.vet;

import java.util.List;

import org.springframework.samples.petclinic.adapters.dtos.vet.VetDto;
import org.springframework.samples.petclinic.adapters.dtos.vet.VetsResponse;
import org.springframework.samples.petclinic.adapters.mappers.vet.VetMapper;
import org.springframework.samples.petclinic.core.domain.vet.Vet;
import org.springframework.samples.petclinic.core.usecases.vet.ListVetsUseCase;
import org.springframework.samples.petclinic.core.usecases.vet.PagedResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Adapter de entrada (web) para o módulo de veterinários.
 */
@Controller
class VetController {

	private static final int PAGE_SIZE = 5;

	private final ListVetsUseCase listVetsUseCase;

	VetController(ListVetsUseCase listVetsUseCase) {
		this.listVetsUseCase = listVetsUseCase;
	}

	@GetMapping("/vets.html")
	public String showVetList(@RequestParam(defaultValue = "1") int page, Model model) {
		PagedResult<Vet> paginated = listVetsUseCase.listPaginated(page, PAGE_SIZE);
		List<VetDto> listVets = paginated.getContent().stream().map(VetMapper::toDto).toList();
		model.addAttribute("currentPage", paginated.getCurrentPage());
		model.addAttribute("totalPages", paginated.getTotalPages());
		model.addAttribute("totalItems", paginated.getTotalElements());
		model.addAttribute("listVets", listVets);
		return "vets/vetList";
	}

	@GetMapping({ "/vets" })
	public @ResponseBody VetsResponse showResourcesVetList() {
		List<VetDto> dtos = listVetsUseCase.listAll().stream().map(VetMapper::toDto).toList();
		return new VetsResponse(dtos);
	}

}
