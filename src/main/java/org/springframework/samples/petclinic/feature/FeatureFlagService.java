package org.springframework.samples.petclinic.feature;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FeatureFlagService {

	private final FeatureFlagRepository repository;

	public FeatureFlagService(FeatureFlagRepository repository) {
		this.repository = repository;
	}

	/* ===================== CRUD ===================== */

	public List<FeatureFlag> findAll() {
		return repository.findAll();
	}

	public Optional<FeatureFlag> findById(Integer id) {
		return repository.findById(id);
	}

	public FeatureFlag save(FeatureFlag flag) {
		return repository.save(flag);
	}

	public void deleteById(Integer id) {
		repository.deleteById(id);
	}

	/* ===================== FEATURE CHECK ===================== */

	@Transactional(readOnly = true)
	public boolean isEnabled(String flagName, String user) {

		Optional<FeatureFlag> flagOpt = repository.findByName(flagName);

		if (flagOpt.isEmpty()) {
			return false;
		}

		FeatureFlag flag = flagOpt.get();

		if (!flag.isEnabled()) {
			return false;
		}

		return switch (flag.getStrategy()) {
			case BOOLEAN -> true;
			case PERCENTAGE -> Math.abs(user.hashCode()) % 100 < flag.getPercentage();
			case WHITELIST -> flag.getUserList() != null && flag.getUserList().contains(user);
			case BLACKLIST -> flag.getUserList() == null || !flag.getUserList().contains(user);
		};
	}

}
