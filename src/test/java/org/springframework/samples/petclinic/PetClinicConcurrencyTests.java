package org.springframework.samples.petclinic;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PetClinicConcurrencyTests {

	@LocalServerPort
	private int port;

	@Autowired
	private OwnerRepository ownerRepository;

	@Autowired
	private RestTemplateBuilder restTemplateBuilder;

	@Test
	public void testDuplicatePetNameRaceConditionIsBlocked() throws Exception {
		int ownerId = 1;
		Optional<Owner> initialOwnerOpt = ownerRepository.findById(ownerId);
		assertThat(initialOwnerOpt).isPresent();
		Owner owner = initialOwnerOpt.get();

		int initialPetCount = owner.getPets().size();
		String duplicatePetName = "ConcurrencyTestPet";

		// Ensure duplicate pet name does not exist yet
		assertThat(owner.getPet(duplicatePetName)).isNull();

		RestTemplate template = restTemplateBuilder.baseUri("http://localhost:" + port).build();

		int threadCount = 2;
		ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
		CountDownLatch readyLatch = new CountDownLatch(threadCount);
		CountDownLatch startLatch = new CountDownLatch(1);
		CountDownLatch doneLatch = new CountDownLatch(threadCount);

		AtomicInteger successCount = new AtomicInteger(0);
		AtomicInteger failureCount = new AtomicInteger(0);

		for (int i = 0; i < threadCount; i++) {
			executorService.submit(() -> {
				readyLatch.countDown();
				try {
					startLatch.await(); // Wait to start simultaneously

					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

					MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
					map.add("name", duplicatePetName);
					map.add("birthDate", "2020-01-01");
					map.add("type", "cat");

					HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

					ResponseEntity<String> response = template.postForEntity("/owners/" + ownerId + "/pets/new",
							request, String.class);

					String body = response.getBody();
					// If the response page contains the duplicate validation error, it
					// was blocked
					if (response.getStatusCode().is2xxSuccessful()
							&& (body == null || !body.contains("is already in use"))) {
						successCount.incrementAndGet();
					}
					else {
						failureCount.incrementAndGet();
					}
				}
				catch (Exception e) {
					failureCount.incrementAndGet();
				}
				finally {
					doneLatch.countDown();
				}
			});
		}

		try {
			boolean ready = readyLatch.await(10, TimeUnit.SECONDS);
			assertThat(ready).isTrue();
			startLatch.countDown();
			boolean completed = doneLatch.await(10, TimeUnit.SECONDS);
			assertThat(completed).isTrue();
		}
		finally {
			executorService.shutdown();
		}

		Owner updatedOwner = ownerRepository.findById(ownerId).get();
		int newPetCount = updatedOwner.getPets().size();

		System.out.println("--- Concurrency Test Assertions ---");
		System.out.println("Successful additions: " + successCount.get());
		System.out.println("Failed additions: " + failureCount.get());
		System.out.println("Original Pet Count: " + initialPetCount);
		System.out.println("Final Pet Count: " + newPetCount);

		// With the fix, exactly ONE concurrent request must succeed
		assertThat(successCount.get()).isEqualTo(1);
		assertThat(newPetCount).isEqualTo(initialPetCount + 1);

		long countWithDuplicateName = updatedOwner.getPets()
			.stream()
			.filter(p -> duplicatePetName.equalsIgnoreCase(p.getName()))
			.count();
		assertThat(countWithDuplicateName).isEqualTo(1);
	}

}
