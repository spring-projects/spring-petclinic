package org.springframework.cheapy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cheapy.model.FoodOffer;
import org.springframework.cheapy.model.StatusOffer;
import org.springframework.cheapy.repository.FoodOfferRepository;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class FoodOfferService {
	
	private FoodOfferRepository foodOfferRepository;

	@Autowired
	public FoodOfferService(final FoodOfferRepository foodOfferRepository) {
		this.foodOfferRepository = foodOfferRepository;
	}

	public FoodOffer findFoodOfferById(final int id) {
		return this.foodOfferRepository.findById(id);
	}
	
  	public List<FoodOffer> findAllFoodOffer() { //
		return this.foodOfferRepository.findAllFoodOffer();
	}

	public void saveFoodOffer(final FoodOffer foodOffer) throws DataAccessException {
		this.foodOfferRepository.save(foodOffer);
	}
	
	public List<FoodOffer> findActiveFoodOffer() {
		return this.foodOfferRepository.findActiveFoodOffer(StatusOffer.active);
	}
	
	public List<FoodOffer> findFoodOfferByUserId(final int id) {
		return this.foodOfferRepository.findByUserId(id);
	}
	
	public List<FoodOffer> findFoodOfferActOclByUserId(final int id) {
		return this.foodOfferRepository.findFoodOfferActOclByUserId(id);
	}
}
