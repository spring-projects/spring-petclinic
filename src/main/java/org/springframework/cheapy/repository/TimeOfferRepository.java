package org.springframework.cheapy.repository;

import org.springframework.cheapy.model.TimeOffer;
import org.springframework.data.repository.Repository;


public interface TimeOfferRepository extends Repository<TimeOffer, Integer> {




	
	TimeOffer findTimeOfferById(int timeOfferId);


	void save(TimeOffer timeOffer);

}
