package org.springframework.samples.petclinic.adapters.web.vet;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.samples.petclinic.adapters.web.vet.dtos.VetDto;
import org.springframework.samples.petclinic.adapters.web.vet.dtos.VetsResponse;
import org.springframework.samples.petclinic.adapters.web.vet.mappers.VetMapper;
import org.springframework.samples.petclinic.core.domain.vet.Vet;
import org.springframework.samples.petclinic.core.usecases.vet.ports.ListVetsPort;
import org.springframework.samples.petclinic.core.usecases.vet.PagedResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
class VetController {

	private static final int PAGE_SIZE = 5;

	private final ListVetsPort listVetsPort;

	@GetMapping("/vets.html")
	public String showVetList(@RequestParam(defaultValue = "1") int page, Model model) {
		PagedResult<Vet> paginated = listVetsPort.listPaginated(page, PAGE_SIZE);
		List<VetDto> listVets = paginated.getContent().stream().map(VetMapper::toDto).toList();
		model.addAttribute("currentPage", paginated.getCurrentPage());
		model.addAttribute("totalPages", paginated.getTotalPages());
		model.addAttribute("totalItems", paginated.getTotalElements());
		model.addAttribute("listVets", listVets);
		return "vets/vetList";
	}

	@GetMapping({ "/vets" })
	public @ResponseBody VetsResponse showResourcesVetList() {
		List<VetDto> dtos = listVetsPort.listAll().stream().map(VetMapper::toDto).toList();
		return new VetsResponse(dtos);
	}

}
