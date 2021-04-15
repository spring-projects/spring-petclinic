
package org.springframework.cheapy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cheapy.model.Municipio;
import org.springframework.cheapy.model.StatusOffer;
import org.springframework.cheapy.model.TimeOffer;
import org.springframework.cheapy.repository.TimeOfferRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
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

	public List<TimeOffer> findAllTimeOffer(final Pageable page) {
		return this.timeOfferRepository.findAllTimeOffer(page);
	}

	public void saveTimeOffer(final TimeOffer TimeOffer) throws DataAccessException {
		this.timeOfferRepository.save(TimeOffer);
	}

	public List<TimeOffer> findActiveTimeOffer(final Pageable p) {
		return this.timeOfferRepository.findActiveTimeOffer(StatusOffer.active, p);
	}

	public List<TimeOffer> findTimeOfferByUserId(final int id) {
		return this.timeOfferRepository.findByUserId(id);
	}

	public List<TimeOffer> findTimeOfferActOclByUserId(final int id) {
		return this.timeOfferRepository.findTimeOfferActOclByUserId(id);
	}

	public List<TimeOffer> findTimeOfferByClientName(final String name) {
		String nameEdit = "%" + name + "%";
		return this.timeOfferRepository.findTimeOfferByClientName(nameEdit);
	}

	public List<TimeOffer> findTimeOfferByClientFood(final String name) {
		String nameEdit = "%" + name + "%";
		return this.timeOfferRepository.findTimeOfferByClientFood(nameEdit);
	}

	public List<TimeOffer> findTimeOfferByClientPlace(final Municipio mun) {
		return this.timeOfferRepository.findTimeOfferByClientPlace(mun);
	}
}
