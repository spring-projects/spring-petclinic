/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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