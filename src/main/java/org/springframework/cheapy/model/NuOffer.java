package org.springframework.cheapy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;
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
	@NotNull
	@Min(0)
	private Integer	discountGold;

	@NotNull
	@Min(1)
	private Integer	silver;

	@Column(name = "discount_silver")
	@NotNull
	@Min(0)
	private Integer	discountSilver;

	@NotNull
	@Min(1)
	private Integer	bronze;

	@Column(name = "discount_bronze")
	@NotNull
	@Min(0)
	private Integer	discountBronze;

	public Integer getGold() {
		return gold;
	}

	public void setGold(Integer gold) {
		this.gold = gold;
	}

	public Integer getDiscountGold() {
		return discountGold;
	}

	public void setDiscountGold(Integer discountGold) {
		this.discountGold = discountGold;
	}

	public Integer getSilver() {
		return silver;
	}

	public void setSilver(Integer silver) {
		this.silver = silver;
	}

	public Integer getDiscountSilver() {
		return discountSilver;
	}

	public void setDiscountSilver(Integer discountSilver) {
		this.discountSilver = discountSilver;
	}

	public Integer getBronze() {
		return bronze;
	}

	public void setBronze(Integer bronze) {
		this.bronze = bronze;
	}

	public Integer getDiscountBronze() {
		return discountBronze;
	}

	public void setDiscountBronze(Integer discountBronze) {
		this.discountBronze = discountBronze;
	}

}
