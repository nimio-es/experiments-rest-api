package io.theam.cucumber;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.theam.App;
import io.theam.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@ContextConfiguration(classes = App.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StepsDefinitions {

    private static final String CUSTOMER_BASE_PATH = "/customers";

    // TODO: Review this, because I think isn't the best way to do this
    private static ResponseEntity<Customer> lastResponse = null;

    @Autowired
    private TestRestTemplate testRestTemplate;

    // ----

    @Given("^a system with one customer in the list$")
    public void a_system_with_one_customer_in_the_list() throws Throwable {
        final Customer customer = new Customer();
        customer.setFirstName("Saulo");
        customer.setFamilyName("Alvarado");
        customer.setNdi("0000000X");
        final HttpEntity<Customer> request = new HttpEntity<>(customer);
        Customer saved = testRestTemplate.postForObject(CUSTOMER_BASE_PATH, request, Customer.class);
        assertThat(saved, notNullValue());
    }

    @When("^asking for customer (\\d+)$")
    public void the_client_calls_customers_X(int id) {
        lastResponse = testRestTemplate.getForEntity(CUSTOMER_BASE_PATH + "/" + Integer.toString(id), Customer.class);
    }

    @Then("^the system responds correctly$")
    public void the_system_responds_correctly() {
        assertThat(lastResponse.getStatusCode().value(), is(200));
    }

    @Then("^the client receives correct customer number (\\d+) information$")
    public void the_client_receives_correct_customer_number_information(int arg1) throws Throwable {
        assertThat(lastResponse.getBody().getFirstName(), is("Saulo"));
        assertThat(lastResponse.getBody().getFamilyName(), is("Alvarado"));
        assertThat(lastResponse.getBody().getNdi(), is("0000000X"));
    }

    @Then("^the system responds that customer was not found$")
    public void the_system_responds_that_customer_was_not_find() throws Throwable {
        assertThat(lastResponse.getStatusCode().value(), is(404));
    }

}
