package org.springframework.cheapy.model;

import java.time.LocalTime;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "clients")
public class Client extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// (id, email, address, init, finish, telephone, description, code, food, usuar)
	@NotEmpty
	private String email;
	
	@NotEmpty
	private String address;

	//@DateTimeFormat(pattern = "HH:mm")
    @NotBlank
    private String init;

    //@DateTimeFormat(pattern = "HH:mm")
    @NotBlank
    private String finish;

	@NotEmpty
	@Digits(fraction = 0, integer = 10)
	private String telephone;

	@NotEmpty
	private String description;

	@NotEmpty
	private String code;

	@NotEmpty
	private String food;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "username", referencedColumnName = "username")
	private User usuar;
	
	@OneToMany
	private Set<FoodOffer> foodOffers;
	
	@OneToMany
	private Set<NuOffer> nuOffers;
	
	@OneToMany
	private Set<SpeedOffer> speedOffers;
	
	@OneToMany
	private Set<TimeOffer> timeOffers;
	
	
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}


	public String getInit() {
		return init;
	}

	public void setInit(String init) {
		this.init = init;
	}

	public String getFinish() {
		return finish;
	}

	public void setFinish(String finish) {
		this.finish = finish;
	}

	public User getUsername() {
		return usuar;
	}

	public void setUsername(User username) {
		this.usuar = username;
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

	public Set<FoodOffer> getFoodOffers() {
		return foodOffers;
	}

	public void setFoodOffers(Set<FoodOffer> foodOffers) {
		this.foodOffers = foodOffers;
	}

	public Set<NuOffer> getNuOffers() {
		return nuOffers;
	}

	public void setNuOffers(Set<NuOffer> nuOffers) {
		this.nuOffers = nuOffers;
	}

	public Set<SpeedOffer> getSpeedOffers() {
		return speedOffers;
	}

	public void setSpeedOffers(Set<SpeedOffer> speedOffers) {
		this.speedOffers = speedOffers;
	}

	public Set<TimeOffer> getTimeOffers() {
		return timeOffers;
	}

	public void setTimeOffers(Set<TimeOffer> timeOffers) {
		this.timeOffers = timeOffers;
	}

}