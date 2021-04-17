package org.springframework.cheapy.repository;

import java.util.List;

import org.springframework.cheapy.model.Client;
import org.springframework.cheapy.model.ReviewClient;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ReviewClientRepository extends PagingAndSortingRepository<ReviewClient, Integer> {

	List<ReviewClient> findByBar(String bar);
	
	List<ReviewClient> findAllReviewClientByBar(Pageable p, Client client);
	
	List<ReviewClient> findAllReviewClientByBar(Client client);
	
	ReviewClient findReviewClientById(int id);
}
