package org.springframework.cheapy.repository;

import org.springframework.cheapy.model.FoodOffer;
import org.springframework.cheapy.model.StatusOffer;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface FoodOfferRepository extends Repository<FoodOffer, Integer> {

	@Query("SELECT foodOffer FROM FoodOffer foodOffer")
	@Transactional(readOnly = true)
	List<FoodOffer> findAllFoodOffer();

	@Query("SELECT foodOffer FROM FoodOffer foodOffer WHERE id =:id")
	@Transactional(readOnly = true)
	FoodOffer findById(@Param("id") Integer id);

	void save(FoodOffer foodOffer);
	
	@Query("SELECT foodOffer FROM FoodOffer foodOffer WHERE foodOffer.status =:status")
	@Transactional(readOnly = true)
	List<FoodOffer> findActiveFoodOffer(StatusOffer status);
	
	@Query("SELECT foodOffer FROM FoodOffer foodOffer WHERE foodOffer.client.id =:id")
	@Transactional(readOnly = true)
	List<FoodOffer> findByUserId(@Param("id") Integer id);
}
