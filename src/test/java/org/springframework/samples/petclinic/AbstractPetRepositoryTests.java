package org.springframework.samples.petclinic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * Base class for {@link ClinicService} integration tests.
 * </p>
 * <p>
 * &quot;AbstractClinicTests-context.xml&quot; declares a common
 * {@link javax.sql.DataSource DataSource}. Subclasses should specify
 * additional context locations which declare a
 * {@link org.springframework.transaction.PlatformTransactionManager PlatformTransactionManager}
 * and a concrete implementation of {@link ClinicService}.
 * </p>
 * <p>
 * This class extends {@link AbstractTransactionalJUnit4SpringContextTests},
 * one of the valuable testing support classes provided by the
 * <em>Spring TestContext Framework</em> found in the
 * <code>org.springframework.test.context</code> package. The
 * annotation-driven configuration used here represents best practice for
 * integration tests with Spring. Note, however, that
 * AbstractTransactionalJUnit4SpringContextTests serves only as a convenience
 * for extension. For example, if you do not wish for your test classes to be
 * tied to a Spring-specific class hierarchy, you may configure your tests with
 * annotations such as {@link ContextConfiguration @ContextConfiguration},
 * {@link org.springframework.test.context.TestExecutionListeners @TestExecutionListeners},
 * {@link org.springframework.transaction.annotation.Transactional @Transactional},
 * etc.
 * </p>
 * <p>
 * AbstractClinicTests and its subclasses benefit from the following services
 * provided by the Spring TestContext Framework:
 * </p>
 * <ul>
 * <li><strong>Spring IoC container caching</strong> which spares us
 * unnecessary set up time between test execution.</li>
 * <li><strong>Dependency Injection</strong> of test fixture instances,
 * meaning that we don't need to perform application context lookups. See the
 * use of {@link Autowired @Autowired} on the <code>petRepository</code> instance
 * variable, which uses autowiring <em>by type</em>. As an alternative, we
 * could annotate <code>petRepository</code> with
 * {@link javax.annotation.Resource @Resource} to achieve dependency injection
 * <em>by name</em>.
 * <em>(see: {@link ContextConfiguration @ContextConfiguration},
 * {@link org.springframework.test.context.support.DependencyInjectionTestExecutionListener DependencyInjectionTestExecutionListener})</em></li>
 * <li><strong>Transaction management</strong>, meaning each test method is
 * executed in its own transaction, which is automatically rolled back by
 * default. Thus, even if tests insert or otherwise change database state, there
 * is no need for a teardown or cleanup script.
 * <em>(see: {@link org.springframework.test.context.transaction.TransactionConfiguration @TransactionConfiguration},
 * {@link org.springframework.transaction.annotation.Transactional @Transactional},
 * {@link org.springframework.test.context.transaction.TransactionalTestExecutionListener TransactionalTestExecutionListener})</em></li>
 * <li><strong>Useful inherited protected fields</strong>, such as a
 * {@link org.springframework.jdbc.core.simple.SimpleJdbcTemplate SimpleJdbcTemplate}
 * that can be used to verify database state after test operations or to verify
 * the results of queries performed by application code. An
 * {@link org.springframework.context.ApplicationContext ApplicationContext} is
 * also inherited and can be used for explicit bean lookup if necessary.
 * <em>(see: {@link org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests AbstractJUnit4SpringContextTests},
 * {@link AbstractTransactionalJUnit4SpringContextTests})</em></li>
 * </ul>
 * <p>
 * The Spring TestContext Framework and related unit and integration testing
 * support classes are shipped in <code>spring-test.jar</code>.
 * </p>
 *
 * @author Ken Krebs
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Sam Brannen
 */
public abstract class AbstractPetRepositoryTests {

	@Autowired
	protected PetRepository petRepository;
	
	@Autowired
	protected OwnerRepository ownerRepository;


	@Test
	public void getPetTypes() {
		Collection<PetType> petTypes = this.petRepository.findPetTypes();
		
		PetType t1 = EntityUtils.getById(petTypes, PetType.class, 1);
		assertEquals("cat", t1.getName());
		PetType t4 = EntityUtils.getById(petTypes, PetType.class, 4);
		assertEquals("snake", t4.getName());
	}

	@Test
	public void findPet() {
		Collection<PetType> types = this.petRepository.findPetTypes();
		Pet p7 = this.petRepository.findById(7);
		assertTrue(p7.getName().startsWith("Samantha"));
		assertEquals(EntityUtils.getById(types, PetType.class, 1).getId(), p7.getType().getId());
		assertEquals("Jean", p7.getOwner().getFirstName());
		Pet p6 = this.petRepository.findById(6);
		assertEquals("George", p6.getName());
		assertEquals(EntityUtils.getById(types, PetType.class, 4).getId(), p6.getType().getId());
		assertEquals("Peter", p6.getOwner().getFirstName());
	}

	@Test @Transactional
	public void insertPet() {
		Owner o6 = this.ownerRepository.findById(6);
		int found = o6.getPets().size();
		Pet pet = new Pet();
		pet.setName("bowser");
		Collection<PetType> types = this.petRepository.findPetTypes();
		pet.setType(EntityUtils.getById(types, PetType.class, 2));
		pet.setBirthDate(new DateTime());
		o6.addPet(pet);
		assertEquals(found + 1, o6.getPets().size());
		// both storePet and storeOwner are necessary to cover all ORM tools
		this.petRepository.save(pet);
		this.ownerRepository.save(o6);
		o6 = this.ownerRepository.findById(6);
		assertEquals(found + 1, o6.getPets().size());
	}

	@Test @Transactional
	public void updatePet() throws Exception {
		Pet p7 = this.petRepository.findById(7);
		String old = p7.getName();
		p7.setName(old + "X");
		this.petRepository.save(p7);
		p7 = this.petRepository.findById(7);
		assertEquals(old + "X", p7.getName());
	}

}
