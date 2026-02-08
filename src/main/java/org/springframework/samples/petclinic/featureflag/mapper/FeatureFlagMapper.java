package org.springframework.samples.petclinic.featureflag.mapper;

import org.springframework.samples.petclinic.featureflag.dto.FeatureFlagDTO;
import org.springframework.samples.petclinic.featureflag.entity.FeatureFlag;
import org.springframework.samples.petclinic.featureflag.entity.FlagType;
import org.springframework.stereotype.Component;

@Component
public class FeatureFlagMapper {

	public FeatureFlag toEntity(FeatureFlagDTO dto) {
		FeatureFlag flag = new FeatureFlag();
		flag.setFlagKey(dto.getFlagKey());
		flag.setDescription(dto.getDescription());
		flag.setEnabled(dto.isEnabled());
		flag.setPercentage(dto.getPercentage());
		flag.setWhitelist(dto.getWhitelist());
		flag.setBlacklist(dto.getBlacklist());
		flag.setFlagType(FlagType.valueOf(dto.getFlagType()));
		return flag;
	}

	public FeatureFlagDTO toDto(FeatureFlag flag) {
		FeatureFlagDTO dto = new FeatureFlagDTO();
		dto.setFlagKey(flag.getFlagKey());
		dto.setDescription(flag.getDescription());
		dto.setEnabled(flag.isEnabled());
		dto.setPercentage(flag.getPercentage());
		dto.setWhitelist(flag.getWhitelist());
		dto.setBlacklist(flag.getBlacklist());
		dto.setFlagType(flag.getFlagType().name());
		return dto;
	}

}
