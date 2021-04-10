
package org.springframework.cheapy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cheapy.model.Review;
import org.springframework.cheapy.repository.ReviewRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewService {

	private ReviewRepository reviewRepository;


	@Autowired
	public ReviewService(final ReviewRepository reviewRepository) {
		this.reviewRepository = reviewRepository;
	}

	@Transactional
	public Review findReviewById(final int id) {
		return this.reviewRepository.findReviewById(id);
	}

	@Transactional
	public List<Review> findAllReviews(final Pageable page) {
		return this.reviewRepository.findAllReviews(page);
	}

	@Transactional
	public void saveReview(final Review Review) throws DataAccessException {
		this.reviewRepository.save(Review);
	}

}
