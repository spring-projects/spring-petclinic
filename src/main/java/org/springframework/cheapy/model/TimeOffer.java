package org.springframework.cheapy.model;

import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "time_offers")
public class TimeOffer extends Offer {

	private static final long serialVersionUID = 1L;

	// Oferta por franja horaria

	@DateTimeFormat(pattern = "HH:mm")
	@NotNull(message = "Debe introducir una hora de inicio")
	private LocalTime init;

	@DateTimeFormat(pattern = "HH:mm")
	@NotNull(message = "Debe introducir una hora de fin")
	private LocalTime finish;

	@NotNull(message = "Debe rellenar el descuento")
	@Range(min = 0, max = 100, message = "El descuento debe estar entre 0 y 100 %")
	private Integer discount;

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

	public Integer getDiscount() {
		return discount;
	}

	public void setDiscount(Integer discount) {
		this.discount = discount;
	}

}