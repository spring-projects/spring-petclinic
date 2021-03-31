package org.springframework.cheapy.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Range;

import com.sun.istack.NotNull;

@Entity
@Table(name = "review")
public class Review extends BaseEntity{
	
	private static final long serialVersionUID = 1L;
	
	@NotBlank
	private String opinion;
	
	@NotNull
	@Range(min = 1, max = 5)
	private Integer stars;

	
	public String getOpinion() {
		return opinion;
	}

	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}

	public Integer getStars() {
		return stars;
	}

	public void setStars(Integer stars) {
		this.stars = stars;
	}
	
}
