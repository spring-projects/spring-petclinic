package org.springframework.samples.petclinic.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.samples.petclinic.model.FeatureFlag;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlagDTO {
	@NotBlank
	public String flagKey;
	@NotBlank
	public String name;
	@NotNull
	public Boolean enabled = true;
	public FeatureFlag.StrategyType strategyType = FeatureFlag.StrategyType.GLOBAL;
	public String strategyValue;


}

