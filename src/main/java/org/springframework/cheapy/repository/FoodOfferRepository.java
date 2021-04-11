
package org.springframework.cheapy.repository;

import java.util.List;

import org.springframework.cheapy.model.FoodOffer;
import org.springframework.cheapy.model.StatusOffer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface FoodOfferRepository extends PagingAndSortingRepository<FoodOffer, Integer> {

	@Query("SELECT foodOffer FROM FoodOffer foodOffer")
	@Transactional(readOnly = true)
	List<FoodOffer> findAllFoodOffer();

	@Query("SELECT foodOffer FROM FoodOffer foodOffer WHERE id =:id")
	@Transactional(readOnly = true)
	FoodOffer findByIdFO(@Param("id") Integer id);

	//void save(FoodOffer foodOffer);

	@Query("SELECT foodOffer FROM FoodOffer foodOffer WHERE foodOffer.status =:status")
	@Transactional(readOnly = true)
	List<FoodOffer> findActiveFoodOffer(StatusOffer status, Pageable p);

	@Query("SELECT foodOffer FROM FoodOffer foodOffer WHERE foodOffer.client.id =:id")
	@Transactional(readOnly = true)
	List<FoodOffer> findByUserId(@Param("id") Integer id);

	@Query("SELECT foodOffer FROM FoodOffer foodOffer WHERE foodOffer.client.id =:id AND foodOffer.status!= 'inactive'")
	@Transactional(readOnly = true)
	List<FoodOffer> findFoodOfferActOclByUserId(@Param("id") Integer id);
	
	@Query("SELECT foodOffer FROM FoodOffer foodOffer WHERE foodOffer.client.name LIKE :name AND foodOffer.status= 'active'")
	@Transactional(readOnly = true)
	List<FoodOffer> findFoodOfferByClientName(String name);
	
}
