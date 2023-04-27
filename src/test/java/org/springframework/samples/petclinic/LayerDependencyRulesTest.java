package org.springframework.samples.petclinic;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

class LayerDependencyRulesTest {

	private final JavaClasses importedClasses = new ClassFileImporter()
		.importPackages("org.springframework.samples.petclinic");

	@Test
	void applicationLayerShouldNotDependOnInfrastructureLayer() {
		ArchRule rule = noClasses().that()
			.resideInAPackage("..application..")
			.should()
			.dependOnClassesThat()
			.resideInAPackage("..infrastructure..");

		rule.check(importedClasses);
	}

}
