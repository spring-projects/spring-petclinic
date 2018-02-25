package org.springframework.samples.petclinic.visit;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class VisitRepositoryTest {
	private static final int VISIT_ID = 1;
	private static final String DESCRIPTION = "Visiting a test case";
	private static final Date TODAY = new Date();
	private Visit visit;
	private List<Visit> visitList;
	
	@Autowired
	private VisitRepository visitRepository;
	
	@Before
	public void setUp(){
		visit = new Visit();
		visit.setDescription(DESCRIPTION);
		visit.setPetId(VISIT_ID);
		visit.setDate(TODAY);
	}

	@Test
	public void shouldFindSavedVisitInVisitRepository() {
		visitRepository.save(visit);
		//Get the list of visits associated to VISIT_ID
		visitList = visitRepository.findByPetId(VISIT_ID);

		assertThat(visitList.size()).isEqualTo(1);
		Visit savedVisit = visitList.get(0);

		assertThat((int) savedVisit.getPetId()).isEqualTo(VISIT_ID);
		assertThat(savedVisit.getDescription()).isEqualTo(DESCRIPTION);
		assertThat(savedVisit.getDate()).isEqualTo(TODAY);
	}
}
