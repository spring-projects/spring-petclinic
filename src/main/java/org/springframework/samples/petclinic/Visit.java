package org.springframework.samples.petclinic;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Simple JavaBean domain object representing a visit.
 *
 * @author Ken Krebs
 */
@Entity @Table(name="visits")
public class Visit extends BaseEntity {

	/** Holds value of property date. */
	@Column(name="visit_date")
	private Date date;

	/** Holds value of property description. */
	@NotNull @Size(min = 1)
	@Column(name="description")
	private String description;

	/** Holds value of property pet. */
	@ManyToOne
    @JoinColumn(name = "pet_id")
	private Pet pet;


	/** Creates a new instance of Visit for the current date */
	public Visit() {
		this.date = new Date();
	}


	/** Getter for property date.
	 * @return Value of property date.
	 */
	public Date getDate() {
		return this.date;
	}

	/** Setter for property date.
	 * @param date New value of property date.
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/** Getter for property description.
	 * @return Value of property description.
	 */
	public String getDescription() {
		return this.description;
	}

	/** Setter for property description.
	 * @param description New value of property description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/** Getter for property pet.
	 * @return Value of property pet.
	 */
	public Pet getPet() {
		return this.pet;
	}

	/** Setter for property pet.
	 * @param pet New value of property pet.
	 */
	public void setPet(Pet pet) {
		this.pet = pet;
	}

}
