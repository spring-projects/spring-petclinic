package org.springframework.cheapy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "nu_offers")
public class NuOffer extends Offer {

	//Oferta por numero de comensales
	private static final long serialVersionUID = 1L;
  
	@NotNull
	@Min(1)
	private Integer	gold;

	@Column(name = "discount_gold")
	@NotBlank
	private String	discountGold;

	@NotNull
	@Min(1)
	private Integer	silver;

	@Column(name = "discount_silver")
	@NotBlank
	private String	discountSilver;

	@NotNull
	@Min(1)
	private Integer	bronze;

	@Column(name = "discount_bronze")
	@NotBlank
	private String	discountBronze;


	public Integer getGold() {
		return this.gold;
	}

	public void setGold(final Integer gold) {
		this.gold = gold;
	}

	public String getDiscountGold() {
		return this.discountGold;
	}

	public void setDiscountGold(final String discountGold) {
		this.discountGold = discountGold;
	}

	public Integer getSilver() {
		return this.silver;
	}

	public void setSilver(final Integer silver) {
		this.silver = silver;
	}

	public String getDiscountSilver() {
		return this.discountSilver;
	}

	public void setDiscountSilver(final String discountSilver) {
		this.discountSilver = discountSilver;
	}

	public Integer getBronze() {
		return this.bronze;
	}

	public void setBronze(final Integer bronze) {
		this.bronze = bronze;
	}

	public String getDiscountBronze() {
		return this.discountBronze;
	}

	public void setDiscountBronze(final String discountBronze) {
		this.discountBronze = discountBronze;
	}

}
