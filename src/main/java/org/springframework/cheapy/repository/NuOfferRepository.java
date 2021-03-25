package org.springframework.cheapy.repository;

import org.springframework.cheapy.model.NuOffer;
import org.springframework.data.repository.Repository;


public interface NuOfferRepository extends Repository<NuOffer, Integer> {




	
	NuOffer findNuOfferById(int nuOfferId);


	void save(NuOffer nuOffer);

}
