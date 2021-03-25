package org.springframework.cheapy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cheapy.model.TimeOffer;
import org.springframework.cheapy.repository.TimeOfferRepository;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class TimeOfferService {
	private TimeOfferRepository timeOfferRepository;


	@Autowired
	public TimeOfferService(final TimeOfferRepository TimeOfferRepository) {
		this.timeOfferRepository = TimeOfferRepository;
	}

	public TimeOffer findTimeOfferById(final int id) {
		return this.timeOfferRepository.findTimeOfferById(id);
	}
  
  public List<TimeOffer> findAllTimeOffer() { //
		return this.timeOfferRepository.findAllTimeOffer();
	}

	
	public void saveTimeOffer(final TimeOffer TimeOffer) throws DataAccessException { //
		this.timeOfferRepository.save(TimeOffer);


	}
}
