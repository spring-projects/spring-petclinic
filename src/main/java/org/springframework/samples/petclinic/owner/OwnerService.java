package org.springframework.samples.petclinic.owner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OwnerService {


	@Autowired
	OwnerRepository ownerRepository;


	public Page<Owner> findPaginatedForOwnersLastName(int pageNo, String lastname)
	{

		int pageSize=5;
		Pageable pageable= PageRequest.of(pageNo-1,pageSize);
		return  ownerRepository.findByLastName(lastname,pageable);

	}




}
