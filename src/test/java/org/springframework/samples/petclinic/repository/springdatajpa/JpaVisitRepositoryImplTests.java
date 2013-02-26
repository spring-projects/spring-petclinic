package org.springframework.samples.petclinic.repository.springdatajpa;

import org.junit.runner.RunWith;
import org.springframework.samples.petclinic.repository.AbstractVisitRepositoryTests;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * <p> Integration tests for the {@link JdbcClinicImpl} implementation. </p> <p> </p>
 *
 * @author Thomas Risberg
 * @author Michael Isvy
 */
@ContextConfiguration(locations = {"classpath:spring/business-config.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("spring-data-jpa")
public class JpaVisitRepositoryImplTests extends AbstractVisitRepositoryTests {


}
