package org.springframework.cheapy.repository;

import java.util.List;
import org.springframework.cheapy.model.TimeOffer;
import org.springframework.data.repository.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface TimeOfferRepository extends Repository<TimeOffer, Integer> {

	TimeOffer findTimeOfferById(int timeOfferId);
  
	@Query("SELECT timeOffer FROM TimeOffer timeOffer")
	@Transactional(readOnly = true)
	List<TimeOffer> findAllTimeOffer();

	void save(TimeOffer timeOffer);

}
