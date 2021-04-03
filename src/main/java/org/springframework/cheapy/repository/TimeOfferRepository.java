package org.springframework.cheapy.repository;

import java.util.List;

import org.springframework.cheapy.model.StatusOffer;
import org.springframework.cheapy.model.TimeOffer;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface TimeOfferRepository extends Repository<TimeOffer, Integer> {

	TimeOffer findTimeOfferById(int timeOfferId);
  
	@Query("SELECT timeOffer FROM TimeOffer timeOffer")
	@Transactional(readOnly = true)
	List<TimeOffer> findAllTimeOffer();

	void save(TimeOffer timeOffer);
	
	@Query("SELECT timeOffer FROM TimeOffer timeOffer WHERE timeOffer.status =:status")
	@Transactional(readOnly = true)
	List<TimeOffer> findActiveTimeOffer(StatusOffer status);
	
	@Query("SELECT timeOffer FROM TimeOffer timeOffer WHERE timeOffer.client.id =:id")
	@Transactional(readOnly = true)
	List<TimeOffer> findByUserId(@Param("id") Integer id);
	
	@Query("SELECT timeOffer FROM TimeOffer timeOffer WHERE timeOffer.client.id =:id AND timeOffer.status!= 'inactive'")
	@Transactional(readOnly = true)
	List<TimeOffer> findTimeOfferActOclByUserId(@Param("id") Integer id);
}
