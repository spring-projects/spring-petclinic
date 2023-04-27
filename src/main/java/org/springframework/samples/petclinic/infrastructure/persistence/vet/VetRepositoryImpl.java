package org.springframework.samples.petclinic.infrastructure.persistence.vet;

import java.util.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.petclinic.domain.VetRepository;
import org.springframework.stereotype.Repository;

@Repository
public class VetRepositoryImpl implements VetRepository {

	private final VetDataRepository vetDataRepository;

	public VetRepositoryImpl(VetDataRepository vetDataRepository) {
		this.vetDataRepository = vetDataRepository;
	}

	@Override
	public Page<VetEntity> getVetPage(int page, int pageSize) {
		Pageable pageable = PageRequest.of(page - 1, pageSize);
		return this.vetDataRepository.findAll(pageable);
	}

	@Override
	public Collection<VetEntity> getVets() {
		return this.vetDataRepository.findAll();
	}

}
