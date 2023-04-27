package org.springframework.samples.petclinic.application;

import java.util.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.petclinic.infrastructure.persistence.vet.Vet;
import org.springframework.samples.petclinic.infrastructure.persistence.vet.VetRepository;
import org.springframework.stereotype.Service;

@Service
public class VetService {

	private final VetRepository vetRepository;

	public VetService(VetRepository vetRepository) {
		this.vetRepository = vetRepository;
	}

	public Page<Vet> getVetPage(int page, int pageSize) {
		Pageable pageable = PageRequest.of(page - 1, pageSize);
		return this.vetRepository.findAll(pageable);
	}

	public Collection<Vet> getVets() {
		return this.vetRepository.findAll();
	}

}
