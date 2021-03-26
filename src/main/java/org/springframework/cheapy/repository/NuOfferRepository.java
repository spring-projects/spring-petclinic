package org.springframework.cheapy.repository;

import java.util.List;
import org.springframework.cheapy.model.NuOffer;
import org.springframework.data.repository.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface NuOfferRepository extends Repository<NuOffer, Integer> {

	NuOffer findNuOfferById(int nuOfferId);
  
	@Query("SELECT nuOffer FROM NuOffer nuOffer")
	@Transactional(readOnly = true)
	List<NuOffer> findAllNuOffer();

	void save(NuOffer nuOffer);

}
