
package org.springframework.cheapy.repository;

import java.util.List;

import org.springframework.cheapy.model.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ReviewRepository extends PagingAndSortingRepository<Review, Integer> {

	@Query("SELECT r FROM Review r")
	@Transactional(readOnly = true)
	List<Review> findAllReviews(Pageable p);

	//void save(Review review);

	@Query("SELECT r FROM Review r WHERE id =:id")
	@Transactional(readOnly = true)
	Review findReviewById(@Param("id") Integer id);
}
