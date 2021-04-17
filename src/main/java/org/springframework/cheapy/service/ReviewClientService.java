package org.springframework.cheapy.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cheapy.model.Client;
import org.springframework.cheapy.model.Review;
import org.springframework.cheapy.model.ReviewClient;
import org.springframework.cheapy.repository.ReviewClientRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ReviewClientService {

	private ReviewClientRepository repo;
	
	@Autowired
	public ReviewClientService(ReviewClientRepository repo) {
		super();
		this.repo = repo;
	}
	@Transactional
	public void saveReview(final ReviewClient entity) {
		this.repo.save(entity);
	}
	@Transactional
	public List<ReviewClient> findByClient(String idClient){
		return this.repo.findByBar(idClient);
	}

	public ReviewClient findReviewById(int reviewId) {
		return this.repo.findReviewClientById(reviewId);
	}
	public List<ReviewClient> findAllReviewsByBar(Pageable p, Client client) {
		
		return this.repo.findAllReviewClientByBar(p, client);
	}
}
