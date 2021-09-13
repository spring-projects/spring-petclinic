package org.springframework.samples.petclinic.vet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class VetService {

	@Autowired
	VetRepository vetRepository;

	public Page<Vet> findPaginated(int pageNo)
	{
		int pageSize=5;
		Pageable pageable= PageRequest.of(pageNo-1,pageSize);
		return vetRepository.findAll(pageable);
	}

}
