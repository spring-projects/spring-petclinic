
package org.springframework.samples.petclinic.jpa;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import java.util.Collection;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.Clinic;
import org.springframework.samples.petclinic.Owner;
import org.springframework.samples.petclinic.Pet;
import org.springframework.samples.petclinic.PetType;
import org.springframework.samples.petclinic.Vet;
import org.springframework.samples.petclinic.Visit;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * Provides the following services:
 * <ul>
 * <li>Injects test dependencies, meaning that we don't need to perform
 * application context lookups. See the setClinic() method. Injection uses
 * autowiring by type.</li>
 * <li>Executes each test method in its own transaction, which is automatically
 * rolled back by default. This means that even if tests insert or otherwise
 * change database state, there is no need for a teardown or cleanup script.</li>
 * </ul>
 * <p>
  * </p>
 *
 * @author Rod Johnson
 * @author Sam Brannen
 * @author Michael Isvy
 */

@ContextConfiguration(locations={"classpath:spring/applicationContext-jpa.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class JpaClinicTests {
	
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	protected Clinic clinic;


	@Test
	public void testBogusJpql() {
		try {
			this.entityManager.createQuery("SELECT RUBBISH FROM RUBBISH HEAP").executeUpdate();
			fail("exception was expected because of incorrect SQL statement");
		} catch (Exception e) {
			// expected
		}
	}


	@Test @Transactional
	public void testGetVets() {
		Collection<Vet> vets = this.clinic.getVets();
		
		Vet v1 = EntityUtils.getById(vets, Vet.class, 2);
		assertEquals("Leary", v1.getLastName());
		assertEquals(1, v1.getNrOfSpecialties());
		assertEquals("radiology", (v1.getSpecialties().get(0)).getName());
		Vet v2 = EntityUtils.getById(vets, Vet.class, 3);
		assertEquals("Douglas", v2.getLastName());
		assertEquals(2, v2.getNrOfSpecialties());
		assertEquals("dentistry", (v2.getSpecialties().get(0)).getName());
		assertEquals("surgery", (v2.getSpecialties().get(1)).getName());
	}

	@Test
	public void testGetPetTypes() {
		Collection<PetType> petTypes = this.clinic.getPetTypes();
		
		PetType t1 = EntityUtils.getById(petTypes, PetType.class, 1);
		assertEquals("cat", t1.getName());
		PetType t4 = EntityUtils.getById(petTypes, PetType.class, 4);
		assertEquals("snake", t4.getName());
	}

	@Test
	public void testFindOwners() {
		Collection<Owner> owners = this.clinic.findOwners("Davis");
		assertEquals(2, owners.size());
		owners = this.clinic.findOwners("Daviss");
		assertEquals(0, owners.size());
	}

	@Test
	public void tesFindOwner() {
		Owner o1 = this.clinic.findOwner(1);
		assertTrue(o1.getLastName().startsWith("Franklin"));
		Owner o10 = this.clinic.findOwner(10);
		assertEquals("Carlos", o10.getFirstName());
	}

	@Test
	public void testInsertOwner() {
		Collection<Owner> owners = this.clinic.findOwners("Schultz");
		int found = owners.size();
		Owner owner = new Owner();
		owner.setLastName("Schultz");
		this.clinic.storeOwner(owner);
		// assertTrue(!owner.isNew()); -- NOT TRUE FOR TOPLINK (before commit)
		owners = this.clinic.findOwners("Schultz");
		assertEquals(found + 1, owners.size());
	}

	@Test
	public void testUpdateOwner() throws Exception {
		Owner o1 = this.clinic.findOwner(1);
		String old = o1.getLastName();
		o1.setLastName(old + "X");
		this.clinic.storeOwner(o1);
		o1 = this.clinic.findOwner(1);
		assertEquals(old + "X", o1.getLastName());
	}

	@Test
	public void testFindPet() {
		Collection<PetType> types = this.clinic.getPetTypes();
		Pet p7 = this.clinic.findPet(7);
		assertTrue(p7.getName().startsWith("Samantha"));
		assertEquals(EntityUtils.getById(types, PetType.class, 1).getId(), p7.getType().getId());
		assertEquals("Jean", p7.getOwner().getFirstName());
		Pet p6 = this.clinic.findPet(6);
		assertEquals("George", p6.getName());
		assertEquals(EntityUtils.getById(types, PetType.class, 4).getId(), p6.getType().getId());
		assertEquals("Peter", p6.getOwner().getFirstName());
	}

	@Test  @Transactional
	public void testInsertPet() {
		Owner o6 = this.clinic.findOwner(6);
		int found = o6.getPets().size();
		Pet pet = new Pet();
		pet.setName("bowser");
		Collection<PetType> types = this.clinic.getPetTypes();
		pet.setType(EntityUtils.getById(types, PetType.class, 2));
		pet.setBirthDate(new Date());
		o6.addPet(pet);
		assertEquals(found + 1, o6.getPets().size());
		this.clinic.storeOwner(o6);
		// assertTrue(!pet.isNew()); -- NOT TRUE FOR TOPLINK (before commit)
		o6 = this.clinic.findOwner(6);
		assertEquals(found + 1, o6.getPets().size());
	}

	@Test
	public void testUpdatePet() throws Exception {
		Pet p7 = this.clinic.findPet(7);
		String old = p7.getName();
		p7.setName(old + "X");
		this.clinic.storePet(p7);
		p7 = this.clinic.findPet(7);
		assertEquals(old + "X", p7.getName());
	}

	@Test  @Transactional
	public void testInsertVisit() {
		Pet p7 = this.clinic.findPet(7);
		int found = p7.getVisits().size();
		Visit visit = new Visit();
		p7.addVisit(visit);
		visit.setDescription("test");
		this.clinic.storePet(p7);
		// assertTrue(!visit.isNew()); -- NOT TRUE FOR TOPLINK (before commit)
		p7 = this.clinic.findPet(7);
		assertEquals(found + 1, p7.getVisits().size());
	}
}