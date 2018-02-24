package org.springframework.samples.petclinic.visit;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
		visitList = visitRepository.findByPetId(VISIT_ID);
		
		assertEquals(visitList.size(), 1);
		Visit savedVisit = visitList.get(0);
		assertEquals((int) savedVisit.getPetId(), VISIT_ID);
		assertEquals(savedVisit.getDescription(), DESCRIPTION);
		assertEquals(savedVisit.getDate(), TODAY);
	}
	

}
