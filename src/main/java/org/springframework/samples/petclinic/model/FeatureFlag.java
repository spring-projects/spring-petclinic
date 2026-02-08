package org.springframework.samples.petclinic.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "feature_flags")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeatureFlag {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	@NotBlank
	private String flagKey;

	@NotBlank
	private String name;

	@NotNull
	private boolean enabled = true;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private StrategyType strategyType = StrategyType.GLOBAL;

	@Column(columnDefinition = "TEXT")
	private String strategyValue; // JSON string

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	public enum StrategyType {
		GLOBAL, BLACKLIST, WHITELIST, PERCENTAGE
	}
}

