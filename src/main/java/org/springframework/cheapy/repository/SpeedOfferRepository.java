package org.springframework.cheapy.repository;

import java.util.List;

import org.springframework.cheapy.model.SpeedOffer;
import org.springframework.cheapy.model.StatusOffer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface SpeedOfferRepository extends Repository<SpeedOffer, Integer> {

	@Query("SELECT speedOffer FROM SpeedOffer speedOffer WHERE id =:id")
	@Transactional(readOnly = true)
	SpeedOffer findById(@Param("id") Integer id);
  	
	@Query("SELECT speedOffer FROM SpeedOffer speedOffer")
	@Transactional(readOnly = true)
	List<SpeedOffer> findAllSpeedOffer();
	
	void save(SpeedOffer speedOffer);
	
	@Query("SELECT speedOffer FROM SpeedOffer speedOffer WHERE speedOffer.status =:status")
	@Transactional(readOnly = true)
	List<SpeedOffer> findActiveSpeedOffer(StatusOffer status);
	
	@Query("SELECT speedOffer FROM SpeedOffer speedOffer WHERE speedOffer.client.id =:id")
	@Transactional(readOnly = true)
	List<SpeedOffer> findByUserId(@Param("id") Integer id);
	
	@Query("SELECT speedOffer FROM SpeedOffer speedOffer WHERE speedOffer.client.id =:id AND speedOffer.status!= 'inactive'")
	@Transactional(readOnly = true)
	List<SpeedOffer> findSpeedOfferActOclByUserId(@Param("id") Integer id);
}
