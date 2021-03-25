package org.springframework.cheapy.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cheapy.model.TimeOffer;
import org.springframework.cheapy.model.Owner;
import org.springframework.cheapy.repository.TimeOfferRepository;
import org.springframework.cheapy.repository.OwnerRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class TimeOfferService {
	private TimeOfferRepository timeOfferRepository;


	@Autowired
	public TimeOfferService(final TimeOfferRepository timeOfferRepository) {
		this.timeOfferRepository = timeOfferRepository;
	}

	public TimeOffer findTimeOfferById(final int id) {
		return this.timeOfferRepository.findById(id);
	}

	public List<TimeOffer> findAllTimeOffer() { //
		return this.timeOfferRepository.findAllTimeOffer();

	}
	
	public void saveOwner(final TimeOffer timeOffer) throws DataAccessException { //
		this.timeOfferRepository.save(timeOffer);

	}
}
