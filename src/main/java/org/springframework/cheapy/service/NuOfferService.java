package org.springframework.cheapy.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cheapy.model.NuOffer;
import org.springframework.cheapy.model.Owner;
import org.springframework.cheapy.repository.NuOfferRepository;
import org.springframework.cheapy.repository.OwnerRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class NuOfferService {
	private NuOfferRepository nuOfferRepository;


	@Autowired
	public NuOfferService(final NuOfferRepository nuOfferRepository) {
		this.nuOfferRepository = nuOfferRepository;
	}

	public NuOffer findNuOfferById(final int id) {
		return this.nuOfferRepository.findById(id);
	}

	public List<NuOffer> findAllNuOffer() { //
		return this.nuOfferRepository.findAllNuOffer();

	}
	
	public void saveOwner(final NuOffer nuOffer) throws DataAccessException { //
		this.nuOfferRepository.save(nuOffer);

	}
}
