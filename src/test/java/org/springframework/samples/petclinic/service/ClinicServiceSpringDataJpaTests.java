
package org.springframework.samples.petclinic.service;

import org.junit.runner.RunWith;
import org.springframework.samples.petclinic.config.BusinessConfig;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * <p> Integration test using the 'Spring Data' profile. 
 * @see AbstractClinicServiceTests AbstractClinicServiceTests for more details. </p>
 * @author Michael Isvy
 */

@ContextConfiguration(classes = BusinessConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("spring-data-jpa")
public class ClinicServiceSpringDataJpaTests extends AbstractClinicServiceTests {

}