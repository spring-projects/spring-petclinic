package org.springframework.cheapy.model;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "clients")
public class Client extends BaseEntity {

	private static final long serialVersionUID = 1L;

	// (id, name, email, address, init, finish, telephone, description, code, food,
	// usuar)

	@NotEmpty
	private String name;

	@NotEmpty
	private String email;

	@NotEmpty
	private String address;
	
	@Enumerated(value = EnumType.STRING)
	private Municipio municipio;

	// Hora de apertura del local
	@NotBlank
	private String init;

	// Hora de cierre del local
	@NotBlank
	private String finish;

	@NotEmpty
	@Digits(fraction = 0, integer = 10)
	private String telephone;

	@NotEmpty
	private String description;

	// Codigo de activacion de cuenta
//	@NotEmpty
//	private String code;

	@NotEmpty
	private String food;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "username", referencedColumnName = "username")
	private User usuar;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "code", referencedColumnName = "code")
	private Code cod;

	@OneToMany
	private List<FoodOffer> foodOffers;

	@OneToMany
	private List<NuOffer> nuOffers;

	@OneToMany
	private List<SpeedOffer> speedOffers;

	@OneToMany
	private List<TimeOffer> timeOffers;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

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

	public Municipio getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}

	public Code getCode() {
		return cod;
	}

	public void setCode(Code code) {
		this.cod = code;
	}

	public String getFood() {
		return food;
	}

	public void setFood(String food) {
		this.food = food;
	}

	public User getUsuar() {
		return usuar;
	}

	public void setUsuar(User usuar) {
		this.usuar = usuar;
	}

	public List<FoodOffer> getFoodOffers() {
		return foodOffers;
	}

	public void setFoodOffers(List<FoodOffer> foodOffers) {
		this.foodOffers = foodOffers;
	}

	public List<NuOffer> getNuOffers() {
		return nuOffers;
	}

	public void setNuOffers(List<NuOffer> nuOffers) {
		this.nuOffers = nuOffers;
	}

	public List<SpeedOffer> getSpeedOffers() {
		return speedOffers;
	}

	public void setSpeedOffers(List<SpeedOffer> speedOffers) {
		this.speedOffers = speedOffers;
	}

	public List<TimeOffer> getTimeOffers() {
		return timeOffers;
	}

	public void setTimeOffers(List<TimeOffer> timeOffers) {
		this.timeOffers = timeOffers;
	}

}