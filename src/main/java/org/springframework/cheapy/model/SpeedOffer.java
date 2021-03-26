/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cheapy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "speed_offers")
public class SpeedOffer extends Offer {

	//Ofertar por rapidez comiendo
	@NotNull
	@Min(0)
	private Integer	gold; // x minutos

	@Column(name = "discount_gold")
	@NotBlank
	private String	discountGold;

	@NotNull
	@Min(0)
	private Integer	silver;

	@Column(name = "discount_silver")
	@NotBlank
	private String	discountSilver;

	@NotNull
	@Min(0)
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
