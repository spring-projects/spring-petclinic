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
package org.springframework.samples.petclinic.vet;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
class VetController {

	private static final String DEFAULT_PAGE_SIZE_VALUE = "10";

	private static final int DEFAULT_PAGE_SIZE = Integer.parseInt(DEFAULT_PAGE_SIZE_VALUE);

	private static final List<Integer> PAGE_SIZE_OPTIONS = List.of(DEFAULT_PAGE_SIZE, 20, 30, 40, 50);

	private static final int PAGINATION_WINDOW_SIZE = 10;

	private final VetRepository vetRepository;

	public VetController(VetRepository vetRepository) {
		this.vetRepository = vetRepository;
	}

	@GetMapping("/vets.html")
	public String showVetList(@RequestParam(defaultValue = "1") int page, Model model,
			@RequestParam(name = "size", defaultValue = DEFAULT_PAGE_SIZE_VALUE) int size) {
		// Here we are returning an object of type 'Vets' rather than a collection of Vet
		// objects so it is simpler for Object-Xml mapping
		Vets vets = new Vets();
		int resolvedPage = Math.max(page, 1);
		int pageSize = resolvePageSize(size);
		Page<Vet> paginated = findPaginated(resolvedPage, pageSize);
		vets.getVetList().addAll(paginated.toList());
		return addPaginationModel(resolvedPage, pageSize, paginated, model);
	}

	private String addPaginationModel(int page, int pageSize, Page<Vet> paginated, Model model) {
		List<Vet> listVets = paginated.getContent();
		long totalItems = paginated.getTotalElements();
		int currentItemCount = paginated.getNumberOfElements();
		long startItem = totalItems == 0 ? 0L : (long) ((page - 1L) * pageSize) + 1L;
		long endItem = currentItemCount == 0 ? startItem : Math.min(startItem + currentItemCount - 1L, totalItems);
		int totalPages = paginated.getTotalPages();
		int windowStart = 1;
		int windowEnd = 0;
		List<Integer> pageNumbers = Collections.emptyList();
		boolean showLeadingGap = false;
		boolean showTrailingGap = false;
		if (totalPages > 0) {
			int maxWindowStart = Math.max(1, totalPages - PAGINATION_WINDOW_SIZE + 1);
			windowStart = Math.max(1, Math.min(page, maxWindowStart));
			windowEnd = Math.min(windowStart + PAGINATION_WINDOW_SIZE - 1, totalPages);
			pageNumbers = IntStream.rangeClosed(windowStart, windowEnd).boxed().toList();
			int leadingHiddenCount = windowStart - 1;
			int trailingHiddenCount = totalPages - windowEnd;
			showLeadingGap = leadingHiddenCount > 0;
			showTrailingGap = trailingHiddenCount > 0;
		}
		model.addAttribute("currentPage", page);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("pageSizeOptions", PAGE_SIZE_OPTIONS);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("totalItems", totalItems);
		model.addAttribute("startItem", startItem);
		model.addAttribute("endItem", endItem);
		model.addAttribute("listVets", listVets);
		model.addAttribute("pageNumbers", pageNumbers);
		model.addAttribute("showLeadingGap", showLeadingGap);
		model.addAttribute("showTrailingGap", showTrailingGap);
		return "vets/vetList";
	}

	private Page<Vet> findPaginated(int page, int pageSize) {
		Pageable pageable = PageRequest.of(page - 1, pageSize);
		return vetRepository.findAll(pageable);
	}

	private int resolvePageSize(int requestedSize) {
		return PAGE_SIZE_OPTIONS.contains(requestedSize) ? requestedSize : DEFAULT_PAGE_SIZE;
	}

	@GetMapping({ "/vets" })
	public @ResponseBody Vets showResourcesVetList() {
		// Here we are returning an object of type 'Vets' rather than a collection of Vet
		// objects so it is simpler for JSon/Object mapping
		Vets vets = new Vets();
		vets.getVetList().addAll(this.vetRepository.findAll());
		return vets;
	}

}
