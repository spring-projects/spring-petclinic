package org.springframework.samples.petclinic.aspects;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.Clinic;
import org.springframework.samples.petclinic.aspects.UsageLogAspect;
import org.springframework.samples.petclinic.jpa.JpaClinicTests;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.assertFalse;

/**
 * <p>
 * Tests for the DAO variant based on the shared EntityManager approach. Uses
 * TopLink Essentials (the reference implementation) for testing.
 * </p>
 * <p>
 * Specifically tests usage of an <code>orm.xml</code> file, loaded by the
 * persistence provider through the Spring-provided persistence unit root URL.
 * </p>
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 */
@ContextConfiguration(locations={"classpath:spring/applicationContext-jpa.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class UsageLogAspectTests {

	@Autowired
	private UsageLogAspect usageLogAspect;
	
	@Autowired
	private Clinic clinic;


	@Test
	public void testUsageLogAspectIsInvoked() {
		String name1 = "Schuurman";
		String name2 = "Greenwood";
		String name3 = "Leau";

		assertTrue(this.clinic.findOwners(name1).isEmpty());
		assertTrue(this.clinic.findOwners(name2).isEmpty());

		List<String> namesRequested = this.usageLogAspect.getNamesRequested();
		assertTrue(namesRequested.contains(name1));
		assertTrue(namesRequested.contains(name2));
		assertFalse(namesRequested.contains(name3));
	}

}
