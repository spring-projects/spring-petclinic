package org.springframework.cheapy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "speed_offers")
public class SpeedOffer extends Offer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull
	private Integer gold; // x minutos

	@Column(name = "discount_gold")
	@NotBlank
	private String discountGold;

	@NotNull
	private Integer silver;

	@Column(name = "discount_silver")
	@NotBlank
	private String discountSilver;

	@NotNull
	private Integer bronze;

	@Column(name = "discount_bronze")
	@NotBlank
	private String discountBronze;

	public Integer getGold() {
		return gold;
	}

	public void setGold(Integer gold) {
		this.gold = gold;
	}

	public String getDiscountGold() {
		return discountGold;
	}

	public void setDiscountGold(String discountGold) {
		this.discountGold = discountGold;
	}

	public Integer getSilver() {
		return silver;
	}

	public void setSilver(Integer silver) {
		this.silver = silver;
	}

	public String getDiscountSilver() {
		return discountSilver;
	}

	public void setDiscountSilver(String discountSilver) {
		this.discountSilver = discountSilver;
	}

	public Integer getBronze() {
		return bronze;
	}

	public void setBronze(Integer bronze) {
		this.bronze = bronze;
	}

	public String getDiscountBronze() {
		return discountBronze;
	}

	public void setDiscountBronze(String discountBronze) {
		this.discountBronze = discountBronze;
	}

}