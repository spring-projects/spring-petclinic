package org.springframework.cheapy.repository;

import java.util.List;

import org.springframework.cheapy.model.Review;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ReviewRepository extends Repository<Review, Integer> {

	@Query("SELECT r FROM Review r")
	@Transactional(readOnly = true)
	List<Review> findAllReviews();

	void save(Review review);
	
	@Query("SELECT r FROM Review r WHERE id =:id")
	@Transactional(readOnly = true)
	Review findReviewById(@Param("id") Integer id);
}
