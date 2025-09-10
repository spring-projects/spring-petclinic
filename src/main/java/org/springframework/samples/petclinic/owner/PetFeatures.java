package org.springframework.samples.petclinic.owner;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@Table(name="pet_features")
public class PetFeatures {

	@Id
	@Column(name="pet_id",insertable=false, updatable=false)
	private Integer petId;

	private String temperament;

	@Column(name="skin_color")
	private String skinColor;

	@Column(name="skin_type")
	private String skinType;

	private int height;
	private double weight;

}


