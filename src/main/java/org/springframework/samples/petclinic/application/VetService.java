package org.springframework.samples.petclinic.application;

import java.util.Collection;
import org.springframework.data.domain.Page;
import org.springframework.samples.petclinic.domain.VetRepository;
import org.springframework.samples.petclinic.infrastructure.persistence.vet.VetEntity;
import org.springframework.stereotype.Service;

@Service
public class VetService {

	private final VetRepository vetRepository;

	public VetService(VetRepository vetRepository) {
		this.vetRepository = vetRepository;
	}

	public Page<VetEntity> getVetPage(int page, int pageSize) {
		return this.vetRepository.getVetPage(page, pageSize);
	}

	public Collection<VetEntity> getVets() {
		return this.vetRepository.getVets();
	}

}
