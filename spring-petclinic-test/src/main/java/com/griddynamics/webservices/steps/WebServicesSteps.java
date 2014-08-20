package com.griddynamics.webservices.steps;

import com.griddynamics.web.PetClinicStoreHost;
import com.jayway.restassured.response.Response;
import org.jbehave.core.annotations.Then;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author lzakharova
 */

@Component
@Scope("thread")
public class WebServicesSteps {

    @Autowired
    PetClinicStoreHost petClinicStoreHost;

    private Response response;

    @Then("vets webservice responds with $code code")
    public void checkResponseCode(int code) {
        setResponse();
        assertThat(response.getStatusCode(), equalTo(code));
    }

    @Then("response from vets webservice contains parameter $parameter with value $value")
    public void checkParamInResponse(String parameter, String value) {
        setResponse();
        assertThat(response.getBody().xmlPath().getString(parameter), equalTo(value));
    }

    private void setResponse() {
        response = given().header("Content-Type", "application/xml").
                when().get(petClinicStoreHost.getPetClinicUrl() + "/vets.xml").andReturn();
    }
}
