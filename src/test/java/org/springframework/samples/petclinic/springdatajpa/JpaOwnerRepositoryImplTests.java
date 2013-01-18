
package org.springframework.samples.petclinic.springdatajpa;

import org.junit.runner.RunWith;
import org.springframework.samples.petclinic.AbstractOwnerRepositoryTests;
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
@ActiveProfiles({"jpa","spring-data-jpa"})
public class JpaOwnerRepositoryImplTests extends AbstractOwnerRepositoryTests {
	
}