package org.springframework.samples.petclinic.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "org.springframework.samples.petclinic",
		importOptions = { ImportOption.DoNotIncludeTests.class, DoNotIncludeMvc.class })
public class CleanArchitectureRulesTest {

	private static final String ROOT = "org.springframework.samples.petclinic";

	// core
	private static final String PKG_DOMAIN = ROOT + ".core.domain..";

	private static final String PKG_GATEWAYS = ROOT + ".core.gateways..";

	private static final String PKG_USECASES = ROOT + ".core.usecases..";

	private static final String PKG_PORTS = ROOT + ".core.usecases..ports..";

	// adapters
	private static final String PKG_CONTROLLERS = ROOT + ".adapters.web..";

	private static final String PKG_ENTITIES = ROOT + ".adapters.entities..";

	private static final String PKG_REPOSITORIES = ROOT + ".adapters.repositories..";

	private static final String PKG_DTOS = ROOT + ".adapters.web..dtos..";

	private static final String PKG_FORMATTERS = ROOT + ".adapters.web..";

	// config
	private static final String PKG_CONFIG = ROOT + ".config..";

	// =========================================================================
	// 1. PUREZA DO DOMÍNIO
	// =========================================================================

	@ArchTest
	static final ArchRule dominio_e_java_puro = noClasses().that()
		.resideInAPackage(PKG_DOMAIN)
		.should()
		.dependOnClassesThat()
		.resideOutsideOfPackages(PKG_DOMAIN, "java..", "lombok..")
		.because("O domínio é o núcleo da aplicação e não pode depender de frameworks."
				+ " Owner, Pet, Vet e Visit devem ser POJOs puros.");

	// =========================================================================
	// 2. DEPENDÊNCIAS ENTRE CAMADAS
	// =========================================================================

	@ArchTest
	static final ArchRule dominio_nao_depende_de_adapters = noClasses().that()
		.resideInAPackage(PKG_DOMAIN)
		.should()
		.dependOnClassesThat()
		.resideInAnyPackage(ROOT + ".adapters..")
		.because("O domínio não pode conhecer os adapters.");

	@ArchTest
	static final ArchRule dominio_nao_depende_de_config = noClasses().that()
		.resideInAPackage(PKG_DOMAIN)
		.should()
		.dependOnClassesThat()
		.resideInAPackage(PKG_CONFIG)
		.because("O domínio não pode conhecer a configuração.");

	@ArchTest
	static final ArchRule usecases_nao_dependem_de_adapters = noClasses().that()
		.resideInAPackage(PKG_USECASES)
		.should()
		.dependOnClassesThat()
		.resideInAnyPackage(ROOT + ".adapters..")
		.because("Use cases não podem depender de adapters (controllers, entities, repositories, etc.).");

	@ArchTest
	static final ArchRule gateways_nao_dependem_de_adapters = noClasses().that()
		.resideInAPackage(PKG_GATEWAYS)
		.should()
		.dependOnClassesThat()
		.resideInAnyPackage(ROOT + ".adapters..")
		.because("Gateways são contratos do core e não podem conhecer os adapters.");

	// =========================================================================
	// 3. CONVENÇÕES DE LOCALIZAÇÃO E NOMENCLATURA
	// =========================================================================

	@ArchTest
	static final ArchRule entidades_jpa_em_adapters_entities = classes().that()
		.areAnnotatedWith("jakarta.persistence.Entity")
		.should()
		.resideInAPackage(PKG_ENTITIES)
		.because("@Entity é detalhe de infraestrutura JPA e pertence a adapters.entities.");

	@ArchTest
	static final ArchRule controllers_em_adapters_controllers = classes().that()
		.areAnnotatedWith("org.springframework.stereotype.Controller")
		.or()
		.areAnnotatedWith("org.springframework.web.bind.annotation.RestController")
		.should()
		.resideInAPackage(PKG_CONTROLLERS)
		.because("Controllers são adapters de entrada e pertencem a adapters.controllers.");

	@ArchTest
	static final ArchRule gateways_sao_interfaces = classes().that()
		.resideInAPackage(PKG_GATEWAYS)
		.should()
		.beInterfaces()
		.because("Gateways são contratos abstratos (interfaces) — output ports do core.");

	@ArchTest
	static final ArchRule ports_sao_interfaces = classes().that()
		.resideInAPackage(PKG_PORTS)
		.should()
		.beInterfaces()
		.because("Input Ports são contratos abstratos (interfaces) — driving ports do core.");

	@ArchTest
	static final ArchRule use_cases_com_sufixo_correto = classes().that()
		.resideInAPackage(PKG_USECASES)
		.and()
		.areNotInterfaces()
		.and()
		.haveSimpleNameNotEndingWith("PagedResult")
		.should()
		.haveSimpleNameEndingWith("UseCase")
		.because("Classes de caso de uso devem terminar com 'UseCase'.");

	@ArchTest
	static final ArchRule formatters_em_adapters_formatters = classes().that()
		.implement("org.springframework.format.Formatter")
		.should()
		.resideInAPackage(PKG_FORMATTERS)
		.because("Formatters são detalhes de binding do Spring MVC e pertencem a adapters.formatters.");

	@ArchTest
	static final ArchRule runtime_hints_em_config = classes().that()
		.implement("org.springframework.aot.hint.RuntimeHintsRegistrar")
		.should()
		.resideInAPackage(PKG_CONFIG)
		.because("RuntimeHintsRegistrar é detalhe de infraestrutura GraalVM e pertence a config.");

	@ArchTest
	static final ArchRule repositories_em_adapters_repositories = classes().that()
		.areAnnotatedWith("org.springframework.stereotype.Repository")
		.should()
		.resideInAPackage(PKG_REPOSITORIES)
		.because("Adapters de persistência pertencem a adapters.repositories.");

	@ArchTest
	static final ArchRule dtos_sem_anotacoes_jpa = noClasses().that()
		.resideInAPackage(PKG_DTOS)
		.should()
		.dependOnClassesThat()
		.resideInAnyPackage("jakarta.persistence..")
		.because("DTOs são objetos de transferência e não devem ter anotações JPA.");

	// =========================================================================
	// 4. BOAS PRÁTICAS
	// =========================================================================

	@ArchTest
	static final ArchRule sem_system_out = noClasses().should()
		.accessClassesThat()
		.haveFullyQualifiedName("java.io.PrintStream")
		.because("Use SLF4J/Logback para logging. System.out e System.err são proibidos em código de produção.");

}
