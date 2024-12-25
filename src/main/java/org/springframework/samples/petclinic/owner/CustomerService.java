package org.springframework.samples.petclinic.owner;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

	private final CustomerRepository customerRepository;

	public CustomerService(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	public Page<Customer> findCustomers(String lastName, String location, int page) {
		Pageable pageable = PageRequest.of(page - 1, 5);

		if (location == null || location.isEmpty()) {
			return customerRepository.findByLastNameStartingWith(lastName, pageable);
		}

		return customerRepository.findByLastNameAndLocation(lastName, location, pageable);
	}

}
