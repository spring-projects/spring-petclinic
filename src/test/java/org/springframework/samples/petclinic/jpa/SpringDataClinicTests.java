
package org.springframework.samples.petclinic.jpa;

import org.junit.runner.RunWith;
import org.springframework.samples.petclinic.AbstractClinicTests;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Michael Isvy
 */

@ContextConfiguration(locations={"classpath:spring/applicationContext-dao.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles({"jpa","spring-data-jpa"})
public class SpringDataClinicTests extends AbstractClinicTests {
		
}