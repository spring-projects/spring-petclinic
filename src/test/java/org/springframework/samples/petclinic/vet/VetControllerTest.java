package org.springframework.samples.petclinic.vet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import java.util.Collections;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class VetControllerTest {

	@Mock
	private VetRepository vetRepository;

	@Mock
	private Model model;

	@InjectMocks
	private VetController vetController;

	@BeforeEach
	void setUp() {
	}

	@Test
	@DisplayName("Test showVetList with pagination")
	void testShowVetListWithPagination() {
		int page = 1;
		Pageable pageable = PageRequest.of(page - 1, 5);
		Page<Vet> paginatedVets = new PageImpl<>(Collections.emptyList(), pageable, 0);

		doReturn(paginatedVets).when(vetRepository).findAll(pageable);

		String viewName = vetController.showVetList(page, model);

		assertThat(viewName).isEqualTo("vets/vetList");
		verify(vetRepository).findAll(pageable);
		verify(model).addAttribute("currentPage", page);
		verify(model).addAttribute("totalPages", paginatedVets.getTotalPages());
		verify(model).addAttribute("totalItems", paginatedVets.getTotalElements());
		verify(model).addAttribute("listVets", paginatedVets.getContent());
	}

	@Test
	@DisplayName("Test showVetList without pagination")
	void testShowVetListWithoutPagination() {
		int defaultPage = 1;
		Pageable pageable = PageRequest.of(defaultPage - 1, 5);
		Page<Vet> paginatedVets = new PageImpl<>(Collections.emptyList(), pageable, 0);

		doReturn(paginatedVets).when(vetRepository).findAll(pageable);

		String viewName = vetController.showVetList(defaultPage, model);

		assertThat(viewName).isEqualTo("vets/vetList");
		verify(vetRepository).findAll(pageable);
		verify(model).addAttribute("currentPage", defaultPage);
		verify(model).addAttribute("totalPages", paginatedVets.getTotalPages());
		verify(model).addAttribute("totalItems", paginatedVets.getTotalElements());
		verify(model).addAttribute("listVets", paginatedVets.getContent());
	}

	@Test
	@DisplayName("Test showVetList with non-empty paginated result")
	void testShowVetListWithNonEmptyPagination() {
		int page = 1;
		Pageable pageable = PageRequest.of(page - 1, 5);
		Vet vet = new Vet();
		Page<Vet> paginatedVets = new PageImpl<>(Collections.singletonList(vet), pageable, 1);

		doReturn(paginatedVets).when(vetRepository).findAll(pageable);

		String viewName = vetController.showVetList(page, model);

		assertThat(viewName).isEqualTo("vets/vetList");
		verify(vetRepository).findAll(pageable);
		verify(model).addAttribute("currentPage", page);
		verify(model).addAttribute("totalPages", paginatedVets.getTotalPages());
		verify(model).addAttribute("totalItems", paginatedVets.getTotalElements());
		verify(model).addAttribute("listVets", paginatedVets.getContent());
	}

	@Test
	@DisplayName("Test showVetList with empty database")
	void testShowVetListWithEmptyDatabase() {
		int page = 1;
		Pageable pageable = PageRequest.of(page - 1, 5);
		Page<Vet> paginatedVets = new PageImpl<>(Collections.emptyList(), pageable, 0);

		doReturn(paginatedVets).when(vetRepository).findAll(pageable);

		String viewName = vetController.showVetList(page, model);

		assertThat(viewName).isEqualTo("vets/vetList");
		verify(vetRepository).findAll(pageable);
		verify(model).addAttribute("currentPage", page);
		verify(model).addAttribute("totalPages", paginatedVets.getTotalPages());
		verify(model).addAttribute("totalItems", paginatedVets.getTotalElements());
		verify(model).addAttribute("listVets", paginatedVets.getContent());
	}

}
