package org.springframework.cheapy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cheapy.model.FoodOffer;
import org.springframework.cheapy.repository.FoodOfferRepository;
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

	public void saveFoodOffer(final FoodOffer foodOffer) throws DataAccessException {
		this.foodOfferRepository.save(foodOffer);

	}
}
