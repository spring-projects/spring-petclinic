package org.springframework.cheapy.model;

import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "time_offers")
public class TimeOffer extends Offer {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//Oferta por franja horaria
	@DateTimeFormat(pattern = "HH:mm")
	@NotNull
	private LocalTime init;

	@DateTimeFormat(pattern = "HH:mm")
	@NotNull
	private LocalTime finish;

	@NotBlank
	private String discount;

	public LocalTime getInit() {
		return init;
	}

	public void setInit(LocalTime init) {
		this.init = init;
	}

	public LocalTime getFinish() {
		return finish;
	}

	public void setFinish(LocalTime finish) {
		this.finish = finish;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

}