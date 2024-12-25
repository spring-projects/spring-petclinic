package org.springframework.samples.petclinic.owner;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	// Find customers by last name with pagination
	Page<Customer> findByLastNameStartingWith(String lastName, Pageable pageable);

	// Custom query to filter by last name and location
	@Query("SELECT c FROM Customer c WHERE c.lastName LIKE :lastName% AND (:location IS NULL OR c.location = :location)")
	Page<Customer> findByLastNameAndLocation(@Param("lastName") String lastName, @Param("location") String location,
			Pageable pageable);

}
