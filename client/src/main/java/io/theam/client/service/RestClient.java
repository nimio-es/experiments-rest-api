package io.theam.client.service;

import io.theam.model.Customer;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;

import java.util.Arrays;
import java.util.Collection;

public class RestClient {

    final OAuth2RestTemplate restTemplate;

    public RestClient(final String username, final String password) {

        ResourceOwnerPasswordResourceDetails resourceDetails = new ResourceOwnerPasswordResourceDetails();
        resourceDetails.setUsername(username);
        resourceDetails.setPassword(password);
        resourceDetails.setAccessTokenUri("http://localhost:8080/oauth/token");
        resourceDetails.setClientId("theam");
        resourceDetails.setClientSecret("secret");
        resourceDetails.setGrantType("password");
        resourceDetails.setScope(Arrays.asList("read", "write"));

        DefaultOAuth2ClientContext clientContext = new DefaultOAuth2ClientContext();

        this.restTemplate = new OAuth2RestTemplate(resourceDetails, clientContext);
        restTemplate.setMessageConverters(Arrays.asList(new MappingJackson2HttpMessageConverter()));
    }

    public Collection<Customer> getCustomers() {
        final String getUrl = "http://localhost:8080/customers";

        ResponseEntity<Collection<Customer>> customers =
                restTemplate.getForEntity(
                        getUrl,
                        (Class<Collection<Customer>>)(Class)Collection.class);

        return customers.getBody();
    }

    public Customer getCustomer(final long customerId) {
        final String getUrl = String.format("http://localhost:8080/customers/%d", customerId);
        ResponseEntity<Customer> customer = restTemplate.getForEntity(getUrl, Customer.class);
        return customer.getBody();
    }
}
