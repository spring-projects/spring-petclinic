package org.springframework.samples.petclinic;

import com.couchbase.client.core.error.IndexExistsException;
import com.couchbase.client.java.Cluster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetRepository;
import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.samples.petclinic.vet.VetRepository;
import org.springframework.samples.petclinic.visit.Visit;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

@Component
public class IndexCMDRunner implements CommandLineRunner {

	@Autowired
	private CouchbaseTemplate template;

	@Autowired
	private Cluster cluster;

	@Autowired
	private VetRepository vetRepository;

	@Autowired
	private OwnerRepository ownerRepository;

	@Autowired
	private PetRepository petRepository;

	@Override
	public void run(String... args) throws Exception {

		try {
			// Create a Primary Index to make it easier for you to query Couchbase
			template.getCouchbaseClientFactory().getCluster().queryIndexes()
					.createPrimaryIndex(template.getBucketName());
		}
		catch (IndexExistsException e) {
			System.out.println("Skipping index creation...");
		}

		// clean the whole database before start up
		cluster.query("Delete from `" + template.getBucketName() + "`");

		createVets();
		createOwners();
		createPets();
	}

	private void createVets() {
		vetRepository.save(new Vet("vet-1", "James", "Carter", new HashSet<>()));
		vetRepository.save(new Vet("vet-2", "Helen", "Leary", new HashSet<>(Arrays.asList("radiology"))));
		vetRepository.save(new Vet("vet-3", "Linda", "Douglas", new HashSet<>(Arrays.asList("surgery", "dentistry"))));
		vetRepository.save(new Vet("vet-4", "Rafael", "Ortega", new HashSet<>(Arrays.asList("surgery"))));
		vetRepository.save(new Vet("vet-5", "Sharon", "Jenkins", new HashSet<>(Arrays.asList("radiology"))));
	}

	private void createOwners() {
		ownerRepository.save(new Owner("owner-1", "George", "Franklin", "110 W. Liberty St.", "Madison", "6085551023"));
		ownerRepository.save(new Owner("owner-2", "Betty", "Davis", "638 Cardinal Ave.", "Sun Prairie", "6085551749"));
		ownerRepository
				.save(new Owner("owner-3", "Eduardo", "Rodriquez", "2693 Commerce St.", "McFarland", "6085558763"));
		ownerRepository.save(new Owner("owner-4", "Harold", "Davis", "563 Friendly St.", "Windsor", "6085553198"));
		ownerRepository.save(new Owner("owner-5", "Peter", "McTavish", "2387 S. Fair Way", "Madison", "6085552765"));
		ownerRepository.save(new Owner("owner-6", "Jean", "Coleman", "105 N. Lake St.", "Monona", "6085552654"));
		ownerRepository.save(new Owner("owner-7", "Jeff", "Black", "1450 Oak Blvd.", "Monona", "6085555387"));
		ownerRepository.save(new Owner("owner-8", "Maria", "Escobito", "345 Maple St.", "Madison", "6085557683"));
		ownerRepository
				.save(new Owner("owner-9", "David", "Schroeder", "2749 Blackhawk Trail", "Madison", "6085559435"));
		ownerRepository
				.save(new Owner("owner-10", "Carlos", "Estaban", "2335 Independence La.", "Waunakee", "6085555487"));
	}

	private void createPets() throws Exception {

		petRepository.save(new Pet("pet-1", "Leo", "2010-09-07", "cat", "owner-1", new ArrayList()));
		petRepository.save(new Pet("pet-2", "Basil", "2012-08-06", "hamster", "owner-2", new ArrayList()));
		petRepository.save(new Pet("pet-3", "Rosy", "2011-04-17", "dog", "owner-3", new ArrayList()));
		petRepository.save(new Pet("pet-4", "Jewel", "2010-03-07", "dog", "owner-3", new ArrayList()));
		petRepository.save(new Pet("pet-5", "Iggy", "2010-11-30", "lizard", "owner-4", new ArrayList()));
		petRepository.save(new Pet("pet-6", "George", "2010-01-20", "snake", "owner-5", new ArrayList()));
		petRepository.save(new Pet("pet-7", "Samantha", "2012-09-04", "cat", "owner-6", Arrays.asList(
				new Visit("visit-1", "2013-01-01", "rabies shot"), new Visit("visit-4", "2013-01-04", "spayed"))));
		petRepository.save(new Pet("pet-8", "Max", "2012-09-04", "cat", "owner-6", Arrays.asList(
				new Visit("visit-2", "2013-01-02", "rabies shot"), new Visit("visit-3", "2013-01-03", "neutered"))));

		petRepository.save(new Pet("pet-9", "Lucky", "2011-08-06", "bird", "owner-7", new ArrayList()));
		petRepository.save(new Pet("pet-10", "Mulligan", "2007-02-24", "dog", "owner-8", new ArrayList()));
		petRepository.save(new Pet("pet-11", "Freddy", "2010-03-09", "bird", "owner-9", new ArrayList()));
		petRepository.save(new Pet("pet-12", "Lucky", "2010-06-24", "dog", "owner-10", new ArrayList()));
		petRepository.save(new Pet("pet-13", "Sly", "2012-06-08", "cat", "owner-10", new ArrayList()));
	}

}
