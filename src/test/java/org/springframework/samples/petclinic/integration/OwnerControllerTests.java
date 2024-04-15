package org.springframework.samples.petclinic.integration;

import com.github.javafaker.Faker;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.persistence.EntityManagerFactory;
import org.hamcrest.Matchers;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.samples.petclinic.owner.*;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles(value = "postgres")
class OwnerControllerTests {

	@LocalServerPort
	private Integer port;

	@Container
	@ServiceConnection
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

	@BeforeEach
	void setUp() {
		RestAssured.baseURI = "http://localhost:" + port;
	}

	Faker faker = new Faker();

	@Autowired
	EntityManagerFactory emf;

	@Autowired
	OwnerRepository ownerRepository;

	@Test
	void shouldSaveNewOwnerPet() {

		Owner owner = CreateOwner();

		String newPetName = faker.dog().name();
		given().contentType("multipart/form-data")
			.multiPart("id", "")
			.multiPart("birthDate", "0222-02-02")
			.multiPart("name", newPetName)
			.multiPart("type", "dog")
			.when()
			.post(String.format("/owners/%s/pets/new", owner.getId()))
			.then()
			.statusCode(Matchers.not(Matchers.greaterThan(499)));

		var updatedOwner = ownerRepository.findById(owner.getId());
		assertThat(updatedOwner.getPets()).hasSize(2).extracting(Pet::getName).contains(newPetName);

	}

	@Test
	void shouldGetAllOwners() {

		Owner owner = CreateOwner();

		var ownerLinkMatcher = String.format("**.findAll { node -> node.@href=='/owners/%s'}", owner.getId());

		given().contentType(ContentType.JSON)
			.when()
			.get("/owners")
			.then()
			.contentType(ContentType.HTML)
			.statusCode(200)
			.body(ownerLinkMatcher, Matchers.notNullValue());

	}

	@Test
	void shouldProvideOwnerVaccinationDate() {

		Owner owner = CreateOwner();

		var ownerLinkMatcher = String.format("**.findAll { node -> node.@href=='/owners/%s'}", owner.getId());

		given().contentType(ContentType.JSON)
			.when()
			.get("/owners")
			.then()
			.contentType(ContentType.HTML)
			.statusCode(200)
			.body(ownerLinkMatcher, Matchers.notNullValue());

		assertThat(false).isTrue();

	}

	@NotNull
	private Owner CreateOwner() {
		var owner = new Owner();
		owner.setFirstName(faker.name().firstName());
		owner.setLastName(faker.name().lastName());
		owner.setAddress(faker.address().streetAddress());
		owner.setTelephone("5555555");
		owner.setCity(faker.address().city());

		Pet pet = new Pet();
		pet.setName(faker.dog().name());
		pet.setBirthDate(faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		PetType dog = new PetType();
		dog.setName(faker.dog().name());
		dog.setId(2);
		pet.setType(dog);
		PetVaccine vaccine = new PetVaccine();
		vaccine.setDate(faker.date()
			.past(30, TimeUnit.DAYS, new java.util.Date())
			.toInstant()
			.atZone(ZoneId.systemDefault())
			.toLocalDate());
		pet.addVaccine(vaccine);

		owner.addPet(pet);
		ownerRepository.save(owner);
		return owner;
	}

}
