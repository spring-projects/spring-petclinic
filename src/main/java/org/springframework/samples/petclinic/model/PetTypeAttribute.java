package org.springframework.samples.petclinic.model;

import java.time.LocalDateTime;

import org.springframework.samples.petclinic.owner.PetType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**PetTypeAttribute Entity 
* validations, and logging.
* @Author: Sujeet Sharma
* @Date: 08--9-2025
*/

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "pet_type_attributes")
public class PetTypeAttribute extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pet_type_id", nullable = false)
	@JsonIgnore
	private PetType petType;

	@Column(length = 255)
	private String temperament;

	@Column(name = "length_cm")
	private Double length;

	@Column(name = "weight_kg")
	private Double weight;

	@Column(name = "created_at", updatable = false, insertable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", insertable = false, updatable = false)
	private LocalDateTime updatedAt;

}
