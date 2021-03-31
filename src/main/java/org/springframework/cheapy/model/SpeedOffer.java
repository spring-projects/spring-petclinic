package org.springframework.cheapy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

@Entity
@Table(name = "speed_offers")
public class SpeedOffer extends Offer {

	// Ofertar por rapidez comiendo
	private static final long serialVersionUID = 1L;

	@NotNull(message = "Debe rellenar el tiempo del rango oro")
	@Min(1)
	private Integer gold;

	@Column(name = "discount_gold")
	@NotNull(message = "Debe rellenar el descuento del rango oro")
	@Range(min = 0, max = 100, message = "El descuento debe estar entre 0 y 100 %")
	private Integer discountGold;

	@NotNull(message = "Debe rellenar el tiempo del rango plata")
	@Min(1)
	private Integer silver;

	@Column(name = "discount_silver")
	@NotNull(message = "Debe rellenar el descuento del rango plata")
	@Range(min = 0, max = 100, message = "El descuento debe estar entre 0 y 100 %")
	private Integer discountSilver;

	@NotNull(message = "Debe rellenar el tiempo del rango bronce")
	@Min(1)
	private Integer bronze;

	@Column(name = "discount_bronze")
	@NotNull(message = "Debe rellenar el descuento del rango bronce")
	@Range(min = 0, max = 100, message = "El descuento debe estar entre 0 y 100 %")
	private Integer discountBronze;

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
