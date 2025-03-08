package org.springframework.samples.petclinic.course;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.vet.Vet;

@Getter
@Setter
@Entity
@Table(name = "courses")
public class Course extends BaseEntity {
	private String name;
	private String description;
	// 예: 초급, 중급, 고급
	private String difficulty;

	@ManyToOne
	@JoinColumn(name = "vet_id")
	private Vet instructor;

}
