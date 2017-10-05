package org.springframework.samples.petclinic.owner;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Ignore
public class OwnerControllerITCase {

    @Autowired
    private TestRestTemplate restTemplate;

    /**
     * Note: This test exists only for the purpose of serving as an example! Please write tests more sensible
     * than this for your own applications.
     */
    @Test
    public void assertsUser1IsGeorgeFranklin() {
        String body = this.restTemplate.getForObject("/owners/1", String.class);
        assertThat(body).contains("<td><b>George Franklin</b></td>");
    }
}
