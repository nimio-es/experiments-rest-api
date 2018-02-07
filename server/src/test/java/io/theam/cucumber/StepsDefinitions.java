package io.theam.cucumber;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.theam.App;
import io.theam.model.Customer;
import io.theam.model.api.CustomerData;
import io.theam.model.api.CustomerResponse;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@ContextConfiguration(classes = App.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StepsDefinitions {

    private static final String CUSTOMER_BASE_PATH = "/customers";

    /* ******************************************************************************************
     *  DISCLAIMER: I know, this isn't the best way to write BDD tests. But the time is money...
     * *************************************************************************************** ** */

    // TODO: Review this, because I think isn't the best way to do this
    private static ResponseEntity<CustomerResponse> lastResponse = null;
    private static String token;

    @Autowired
    private TestRestTemplate testRestTemplate;

    // ----

    private String getToken() {
        final MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
        request.set("username", "noelia.capaz");
        request.set("password", "password");
        request.set("grant_type", "password");

        Map<String, Object> response =
                testRestTemplate.withBasicAuth("theam", "secret")
                        .postForObject("/oauth/token", request, Map.class);

        for(Map.Entry<String, Object> e: response.entrySet())
            LoggerFactory.getLogger(StepsDefinitions.class).info("entry = {}, {}", e.getKey(), e.getValue().toString());

        return response.get("access_token").toString();
    }

    @Given("^a user with a token$")
    public void a_user_with_a_token() {
        token = getToken();
    }

    @Given("^a system with some customers in the list$")
    public void a_system_with_one_customer_in_the_list() {
        final CustomerData customer = new CustomerData("Paco", "Mer", "0000000X");
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        final HttpEntity<CustomerData> request = new HttpEntity<>(customer, headers);
        CustomerResponse saved = testRestTemplate.postForObject(CUSTOMER_BASE_PATH, request, CustomerResponse.class);
        assertThat(saved.getCustomerId(), notNullValue());
    }

    @When("^asking for customer (\\d+)$")
    public void the_client_calls_customers_X(int id) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);

        lastResponse = testRestTemplate.
                exchange(
                        CUSTOMER_BASE_PATH + "/" + Long.toString(id),
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        CustomerResponse.class);
    }

    @Then("^the system responds correctly$")
    public void the_system_responds_correctly() {
        assertThat(lastResponse.getStatusCode().value(), is(200));
    }

    @Then("^the client receives correct customer number (\\d+) information$")
    public void the_client_receives_correct_customer_number_information(int arg1) {
        // this one preexists
        assertThat(lastResponse.getBody().getCustomer().getFirstName(), is("Saulo"));
        assertThat(lastResponse.getBody().getCustomer().getLastName(), is("Alvarado Mateos"));
        assertThat(lastResponse.getBody().getCustomer().getNdi(), is("000000000X"));
    }

    @Then("^the system responds that customer was not found$")
    public void the_system_responds_that_customer_was_not_find() {
        assertThat(lastResponse.getStatusCode().value(), is(404));
    }

}
