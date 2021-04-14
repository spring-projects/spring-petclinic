
package org.springframework.cheapy.repository;

import java.util.List;

import org.springframework.cheapy.model.StatusOffer;
import org.springframework.cheapy.model.TimeOffer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface TimeOfferRepository extends PagingAndSortingRepository<TimeOffer, Integer> {
	
	@Query("SELECT timeOffer FROM TimeOffer timeOffer WHERE timeOffer.id =:id")
	@Transactional(readOnly = true)
	TimeOffer findTimeOfferById(int id);

	@Query("SELECT timeOffer FROM TimeOffer timeOffer")
	@Transactional(readOnly = true)
	List<TimeOffer> findAllTimeOffer(Pageable p);

	//void save(TimeOffer timeOffer);

	@Query("SELECT timeOffer FROM TimeOffer timeOffer WHERE timeOffer.status =:status")
	@Transactional(readOnly = true)
	List<TimeOffer> findActiveTimeOffer(StatusOffer status, Pageable p);

	@Query("SELECT timeOffer FROM TimeOffer timeOffer WHERE timeOffer.client.id =:id")
	@Transactional(readOnly = true)
	List<TimeOffer> findByUserId(@Param("id") Integer id);

	@Query("SELECT timeOffer FROM TimeOffer timeOffer WHERE timeOffer.client.id =:id AND timeOffer.status!= 'inactive'")
	@Transactional(readOnly = true)
	List<TimeOffer> findTimeOfferActOclByUserId(@Param("id") Integer id);
	
	@Query("SELECT timeOffer FROM TimeOffer timeOffer WHERE timeOffer.client.name LIKE :name AND timeOffer.status= 'active'")
	@Transactional(readOnly = true)
	List<TimeOffer> findTimeOfferByClientName(String name);
	
	@Query("SELECT timeOffer FROM TimeOffer timeOffer WHERE timeOffer.client.food LIKE :name AND timeOffer.status= 'active'")
	@Transactional(readOnly = true)
	List<TimeOffer> findTimeOfferByClientFood(String name);
}
