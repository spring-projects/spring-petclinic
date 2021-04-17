package org.springframework.cheapy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

@Entity
@Table(name = "review_client")
public class ReviewClient extends BaseEntity{

private static final long serialVersionUID = 1L;
	
	@NotBlank(message = "Debe rellenar la valoración sobre el bar")
	@Column(length=16777215)
	private String opinion;
	
	@NotNull(message= "Por favor rellene este campo")
	@Range(min = 1, max = 5,message="Las estrellas deben ir entre 1 y 5")
	private Integer stars;

	@ManyToOne
	@JoinColumn(name = "username", referencedColumnName = "username")
	private User escritor;
	
	@ManyToOne
	@JoinColumn(name = "client", referencedColumnName = "id")
	private Client bar;
	
	public User getEscritor() {
		return escritor;
	}

	public Client getBar() {
		return bar;
	}

	public void setBar(Client bar) {
		this.bar = bar;
	}

	public void setEscritor(User escritor) {
		this.escritor = escritor;
	}

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
