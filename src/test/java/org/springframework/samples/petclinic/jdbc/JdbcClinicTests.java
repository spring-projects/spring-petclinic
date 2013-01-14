package org.springframework.samples.petclinic.jdbc;

import org.junit.runner.RunWith;
import org.springframework.samples.petclinic.AbstractClinicTests;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * <p>
 * Integration tests for the {@link JdbcClinic} implementation.
 * </p>
 * <p>
 * "JdbcClinicTests-context.xml" determines the actual beans to test.
 * </p>
 *
 * @author Thomas Risberg
 */
@ContextConfiguration(locations={"classpath:spring/applicationContext-jdbc.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
public class JdbcClinicTests extends AbstractClinicTests {
	
	

}
