package org.springframework.cheapy.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "clients")
public class Client extends User {

	@NotEmpty
	private String address;

	@NotEmpty
	private String timetable;

	@NotEmpty
	@Digits(fraction = 0, integer = 10)
	private String telephone;

	@NotEmpty
	private String description;

	@NotEmpty
	private String code;

	@NotEmpty
	private String food;
	
	@OneToMany
	private Set<FoodOffer> foodOffers;
	
	@OneToMany
	private Set<NuOffer> nuOffers;
	
	@OneToMany
	private Set<SpeedOffer> speedOffers;
	
	@OneToMany
	private Set<TimeOffer> timeOffers;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTimetable() {
		return timetable;
	}

	public void setTimetable(String timetable) {
		this.timetable = timetable;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getFood() {
		return food;
	}

	public void setFood(String food) {
		this.food = food;
	}
}