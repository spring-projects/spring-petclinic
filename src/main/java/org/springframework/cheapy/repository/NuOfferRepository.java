
package org.springframework.cheapy.repository;

import java.util.List;

import org.springframework.cheapy.model.NuOffer;
import org.springframework.cheapy.model.StatusOffer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface NuOfferRepository extends PagingAndSortingRepository<NuOffer, Integer> {

	NuOffer findNuOfferById(int nuOfferId);

	@Query("SELECT nuOffer FROM NuOffer nuOffer")
	@Transactional(readOnly = true)
	List<NuOffer> findAllNuOffer(Pageable p);

	//void save(NuOffer nuOffer);

	@Query("SELECT nuOffer FROM NuOffer nuOffer WHERE nuOffer.status =:status")
	@Transactional(readOnly = true)
	List<NuOffer> findActiveNuOffer(StatusOffer status, Pageable p);

	@Query("SELECT nuOffer FROM NuOffer nuOffer WHERE nuOffer.client.id =:id")
	@Transactional(readOnly = true)
	List<NuOffer> findByUserId(@Param("id") Integer id);

	@Query("SELECT nuOffer FROM NuOffer nuOffer WHERE nuOffer.client.id =:id AND nuOffer.status!= 'inactive'")
	@Transactional(readOnly = true)
	List<NuOffer> findNuOfferActOclByUserId(@Param("id") Integer id);
}
