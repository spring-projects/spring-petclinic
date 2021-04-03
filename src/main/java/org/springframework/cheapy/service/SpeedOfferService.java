
package org.springframework.cheapy.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cheapy.model.NuOffer;
import org.springframework.cheapy.model.SpeedOffer;
import org.springframework.cheapy.model.StatusOffer;
import org.springframework.cheapy.repository.SpeedOfferRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SpeedOfferService {

	private SpeedOfferRepository speedOfferRepository;


	@Autowired
	public SpeedOfferService(final SpeedOfferRepository speedOfferRepository) {
		this.speedOfferRepository = speedOfferRepository;
	}

	@Transactional
	public SpeedOffer findSpeedOfferById(final int id) {
		return this.speedOfferRepository.findById(id);
	}
  
	@Transactional
	public List<SpeedOffer> findAllSpeedOffer() { //
		return this.speedOfferRepository.findAllSpeedOffer();
	}

	@Transactional
	public void saveSpeedOffer(final SpeedOffer speedOffer) throws DataAccessException { //
		this.speedOfferRepository.save(speedOffer);
	}
	
	public List<SpeedOffer> findActiveSpeedOffer() {
		return this.speedOfferRepository.findActiveSpeedOffer(StatusOffer.active);
	}
	
	public List<SpeedOffer> findSpeedOfferByUserId(final int id) {
		return this.speedOfferRepository.findByUserId(id);
	}
	
	public List<SpeedOffer> findSpeedOfferActOclByUserId(final int id) {
		return this.speedOfferRepository.findSpeedOfferActOclByUserId(id);
	}
}
