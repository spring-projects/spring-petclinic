
package org.springframework.cheapy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cheapy.model.NuOffer;
import org.springframework.cheapy.model.StatusOffer;
import org.springframework.cheapy.repository.NuOfferRepository;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NuOfferService {

	private NuOfferRepository nuOfferRepository;

	@Autowired
	public NuOfferService(final NuOfferRepository nuOfferRepository) {
		this.nuOfferRepository = nuOfferRepository;
	}

	@Transactional
	public NuOffer findNuOfferById(final int id) {
		return this.nuOfferRepository.findNuOfferById(id);
	}

	@Transactional
	public List<NuOffer> findAllNuOffer() {
		return this.nuOfferRepository.findAllNuOffer();
	}

	@Transactional
	public void saveNuOffer(final NuOffer nuOffer) throws DataAccessException {
		this.nuOfferRepository.save(nuOffer);
	}
	
	@Transactional
	public void saveUpdateNuOffer(final NuOffer nuOfferNew, final NuOffer nuOfferOld) throws DataAccessException {
		this.nuOfferRepository.save(nuOfferNew);
	}
	
	public List<NuOffer> findActiveNuOffer() {
		return this.nuOfferRepository.findActiveNuOffer(StatusOffer.active);
	}
	
	public List<NuOffer> findNuOfferByUserId(final int id) {
		return this.nuOfferRepository.findByUserId(id);
	}
	
	public List<NuOffer> findNuOfferActOclByUserId(final int id) {
		return this.nuOfferRepository.findNuOfferActOclByUserId(id);
	}
}
