package org.springframework.samples.petclinic.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.onionArchitecture;

/**
 * Diretrizes arquiteturais da Clean Architecture para o spring-petclinic.
 *
 * <p>
 * As regras estão organizadas em três grupos:
 * <ol>
 * <li><b>Regra de Dependência</b> — validada de forma holística pela API
 * {@code onionArchitecture()} do ArchUnit. Garante que dependências de código-fonte
 * apontam apenas para dentro (em direção ao domínio).</li>
 * <li><b>Pureza do domínio</b> — o núcleo da aplicação não pode ser contaminado por
 * nenhum framework externo (JPA, Spring, Bean Validation, JAXB).</li>
 * <li><b>Convenções de localização e nomenclatura</b> — garantem que cada tipo de
 * artefato (entidade JPA, controller, use case, port) vive no pacote correto e recebe um
 * nome que comunica sua responsabilidade.</li>
 * </ol>
 *
 * <p>
 * <b>Pacotes alvo ainda inexistentes</b> usam {@code allowEmptyShould(true)} para não
 * falhar antes de serem criados na refatoração. À medida que cada módulo for refatorado, o
 * {@code allowEmptyShould} pode ser removido.
 *
 */
@AnalyzeClasses(packages = "org.springframework.samples.petclinic",
		importOptions = ImportOption.DoNotIncludeTests.class)
public class CleanArchitectureRulesTest {

	private static final String ROOT = "org.springframework.samples.petclinic";

	private static final String PKG_DOMAIN = ROOT + ".domain..";

	private static final String PKG_APPLICATION = ROOT + ".application..";

	private static final String PKG_INTERFACES_WEB = ROOT + ".interfaces.web..";

	private static final String PKG_INTERFACES_API = ROOT + ".interfaces.api..";

	private static final String PKG_INFRA_PERSIST = ROOT + ".infrastructure.persistence..";

	private static final String PKG_INFRA_CONFIG = ROOT + ".infrastructure.config..";

	private static final String PKG_PORT_OUT = ROOT + ".application..port.out..";

	private static final String PKG_USE_CASE = ROOT + ".application..usecase..";

	// =========================================================================
	// 1. REGRA DE DEPENDÊNCIA — Clean Architecture (Onion)
	// =========================================================================

	/**
	 * A lei fundamental da Clean Architecture: dependências de código-fonte apontam
	 * apenas para dentro, em direção ao domínio.
	 *
	 * <p>
	 * Esta regra sozinha substitui todas as verificações individuais de dependência entre
	 * camadas (domain ← application ← infrastructure/interfaces). O
	 * {@code withOptionalLayers(true)} permite que camadas ainda não criadas não causem
	 * falha durante a migração incremental.
	 */
	@ArchTest
	static final ArchRule regra_de_dependencia = onionArchitecture().domainModels(PKG_DOMAIN)
		.domainServices(PKG_DOMAIN)
		.applicationServices(PKG_APPLICATION)
		.adapter("web", PKG_INTERFACES_WEB)
		.adapter("api", PKG_INTERFACES_API)
		.adapter("persistence", PKG_INFRA_PERSIST)
		.adapter("config", PKG_INFRA_CONFIG)
		.withOptionalLayers(true)
		.because("Dependências de código-fonte só podem apontar para dentro:"
				+ " interfaces/infrastructure → application → domain."
				+ " Nenhuma camada interna pode conhecer camadas externas.");

	// =========================================================================
	// 2. PUREZA DO DOMÍNIO
	// =========================================================================

	/**
	 * O domínio não pode depender de nenhum framework externo.
	 *
	 * <p>
	 * Uma única regra cobre todas as contaminações conhecidas no código atual:
	 * <ul>
	 * <li>{@code Owner}, {@code Person}, {@code NamedEntity} →
	 * {@code jakarta.persistence.*}, {@code jakarta.validation.constraints.*},
	 * {@code org.springframework.core.style.ToStringCreator}</li>
	 * <li>{@code Pet} → {@code org.springframework.format.annotation.DateTimeFormat}</li>
	 * <li>{@code Vet} → {@code jakarta.xml.bind.annotation.XmlElement}</li>
	 * <li>{@code Vets} → {@code jakarta.xml.bind.annotation.XmlRootElement}</li>
	 * </ul>
	 * Após a refatoração, entidades de domínio são POJOs puros; anotações de framework
	 * migram para {@code infrastructure.persistence} e {@code interfaces}.
	 */
	@ArchTest
	static final ArchRule dominio_e_java_puro = noClasses().that()
		.resideInAPackage(PKG_DOMAIN)
		.should()
		.dependOnClassesThat()
		.resideInAnyPackage("org.springframework..", "jakarta.persistence..", "jakarta.validation..",
				"jakarta.xml.bind..")
		.allowEmptyShould(true)
		.because("O domínio é o núcleo da aplicação e não pode depender de frameworks."
				+ " Owner, Pet, Vet e Visit devem ser POJOs puros sem @Entity, @NotBlank,"
				+ " @DateTimeFormat, @XmlElement etc.");

	// =========================================================================
	// 3. CONVENÇÕES DE LOCALIZAÇÃO E NOMENCLATURA
	// =========================================================================

	/**
	 * Entidades JPA ({@code @Entity}) devem residir exclusivamente em
	 * {@code infrastructure.persistence}.
	 *
	 * <p>
	 * Violações atuais: {@code Owner}, {@code Pet}, {@code PetType}, {@code Visit},
	 * {@code Vet} e {@code Specialty} são {@code @Entity} nos pacotes {@code owner} e
	 * {@code vet}. Devem migrar para {@code OwnerJpaEntity}, {@code PetJpaEntity} etc. em
	 * {@code infrastructure.persistence}.
	 */
	@ArchTest
	static final ArchRule entidades_jpa_em_infrastructure = classes().that()
		.areAnnotatedWith("jakarta.persistence.Entity")
		.should()
		.resideInAPackage(PKG_INFRA_PERSIST)
		.because("@Entity é um detalhe de infraestrutura JPA e pertence a"
				+ " infrastructure.persistence, nunca ao domínio.");

	/**
	 * Controllers ({@code @Controller} / {@code @RestController}) devem residir
	 * exclusivamente em {@code interfaces.web} ou {@code interfaces.api}.
	 *
	 * <p>
	 * Violações atuais: {@code OwnerController}, {@code PetController},
	 * {@code VisitController}, {@code VetController}, {@code WelcomeController} e
	 * {@code CrashController} estão nos pacotes {@code owner}, {@code vet} e
	 * {@code system}.
	 */
	@ArchTest
	static final ArchRule controllers_em_interfaces = classes().that()
		.areAnnotatedWith("org.springframework.stereotype.Controller")
		.or()
		.areAnnotatedWith("org.springframework.web.bind.annotation.RestController")
		.should()
		.resideInAnyPackage(PKG_INTERFACES_WEB, PKG_INTERFACES_API)
		.because("Controllers são adapters de entrada e pertencem a interfaces.web (Thymeleaf)"
				+ " ou interfaces.api (REST). Nunca ao pacote de domínio.");

	/**
	 * Portas de saída em {@code application.port.out} devem ser interfaces com nome
	 * terminado em {@code Port}.
	 *
	 * <p>
	 * Substitui os atuais {@code OwnerRepository extends JpaRepository} e
	 * {@code VetRepository extends Repository} — que acoplam o contrato ao Spring Data —
	 * por interfaces puras como {@code OwnerRepositoryPort} e {@code VetRepositoryPort}.
	 */
	@ArchTest
	static final ArchRule output_ports_sao_interfaces_com_sufixo_port = classes().that()
		.resideInAPackage(PKG_PORT_OUT)
		.should()
		.beInterfaces()
		.andShould()
		.haveSimpleNameEndingWith("Port")
		.allowEmptyShould(true)
		.because("Output Ports são contratos abstratos (interfaces) com sufixo 'Port'."
				+ " A implementação concreta fica nos Adapters em infrastructure.persistence.");

	/**
	 * Use Cases devem ter nome terminado em {@code UseCase} e residir em
	 * {@code application..usecase}.
	 *
	 * <p>
	 * Exemplos esperados: {@code CreateOwnerUseCase}, {@code BookVisitUseCase},
	 * {@code ListVetsUseCase}.
	 */
	@ArchTest
	static final ArchRule use_cases_com_sufixo_correto = classes().that()
		.resideInAPackage(PKG_USE_CASE)
		.should()
		.haveSimpleNameEndingWith("UseCase")
		.allowEmptyShould(true)
		.because("Convenção: classes de caso de uso terminam com 'UseCase' para comunicar"
				+ " sua responsabilidade de forma imediata.");

	/**
	 * Formatters Spring MVC devem residir em {@code interfaces.web}.
	 *
	 * <p>
	 * Violação atual: {@code PetTypeFormatter implements Formatter<PetType>} está no
	 * pacote {@code owner} junto ao domínio. É um detalhe de binding do Spring MVC e
	 * pertence a {@code interfaces.web}.
	 */
	@ArchTest
	static final ArchRule formatters_em_interfaces_web = classes().that()
		.implement("org.springframework.format.Formatter")
		.should()
		.resideInAPackage(PKG_INTERFACES_WEB)
		.because("Formatters são detalhes de binding do Spring MVC e pertencem a"
				+ " interfaces.web. PetTypeFormatter deve migrar para interfaces.web.owner.");

	/**
	 * {@code RuntimeHintsRegistrar} pertence a {@code infrastructure.config}.
	 *
	 * <p>
	 * Violação atual: {@code PetClinicRuntimeHints} está no pacote raiz da aplicação. É
	 * um detalhe de infraestrutura GraalVM e deve residir em
	 * {@code infrastructure.config}.
	 */
	@ArchTest
	static final ArchRule runtime_hints_em_infrastructure_config = classes().that()
		.implement("org.springframework.aot.hint.RuntimeHintsRegistrar")
		.should()
		.resideInAPackage(PKG_INFRA_CONFIG)
		.because("RuntimeHintsRegistrar é detalhe de infraestrutura GraalVM."
				+ " PetClinicRuntimeHints deve migrar para infrastructure.config.");

	/**
	 * Proibido o uso de {@code System.out} e {@code System.err} em código de produção.
	 *
	 * <p>
	 * Todo logging deve ser feito via SLF4J/Logback.
	 */
	@ArchTest
	static final ArchRule sem_system_out = noClasses().should()
		.accessClassesThat()
		.haveFullyQualifiedName("java.io.PrintStream")
		.because("Use SLF4J/Logback para logging. System.out e System.err são proibidos" + " em código de produção.");

}
