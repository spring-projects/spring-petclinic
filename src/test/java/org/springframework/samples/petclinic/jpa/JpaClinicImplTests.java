
package org.springframework.samples.petclinic.jpa;

import static junit.framework.Assert.fail;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.AbstractClinicTests;
import org.springframework.samples.petclinic.Clinic;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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

@ContextConfiguration(locations={"classpath:spring/applicationContext-dao.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles({"jpa","plain-jpa"})
public class JpaClinicImplTests extends AbstractClinicTests {
	
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private Clinic clinic;


	@Test
	public void testBogusJpql() {
		try {
			this.entityManager.createQuery("SELECT RUBBISH FROM RUBBISH HEAP").executeUpdate();
			fail("exception was expected because of incorrect SQL statement");
		} catch (Exception e) {
			// expected
		}
	}
	
}