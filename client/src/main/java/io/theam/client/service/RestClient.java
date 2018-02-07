package io.theam.client.service;

import com.fasterxml.jackson.module.kotlin.ExtensionsKt;
import io.theam.model.api.CustomerData;
import io.theam.model.api.CustomerResponse;
import io.theam.model.api.ImageData;
import io.theam.util.UtilBase64Image;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;

public final class RestClient {

    private static final String base_url =  "http://localhost:8080";
    private static final String base_customers_url = base_url + "/customers";
    private static final String base_customers_image_url = base_customers_url + "/%d/image";

    final OAuth2RestTemplate restTemplate;

    public RestClient(final String username, final String password) {

        ResourceOwnerPasswordResourceDetails resourceDetails = new ResourceOwnerPasswordResourceDetails();
        resourceDetails.setUsername(username);
        resourceDetails.setPassword(password);
        resourceDetails.setAccessTokenUri(base_url + "/oauth/token");
        resourceDetails.setClientId("theam");
        resourceDetails.setClientSecret("secret");
        resourceDetails.setGrantType("password");
        resourceDetails.setScope(Arrays.asList("read", "write"));

        DefaultOAuth2ClientContext clientContext = new DefaultOAuth2ClientContext();

        this.restTemplate = new OAuth2RestTemplate(resourceDetails, clientContext);
        final MappingJackson2HttpMessageConverter mpcv = new MappingJackson2HttpMessageConverter();
        ExtensionsKt.registerKotlinModule(mpcv.getObjectMapper());
        restTemplate.setMessageConverters(Arrays.asList(mpcv));
    }

    public Collection<CustomerResponse> getCustomers() {
        final String getUrl = base_customers_url;

        ResponseEntity<Collection<CustomerResponse>> customers =
                restTemplate.getForEntity(
                        getUrl,
                        (Class<Collection<CustomerResponse>>)(Class)Collection.class);

        return customers.getBody();
    }

    public CustomerResponse getCustomer(final long customerId, final boolean includeImage) {
        final String getUrl = String.format(base_customers_url + "/%d?includeImage=%s", customerId, Boolean.toString(includeImage).toLowerCase());
        ResponseEntity<CustomerResponse> customer = restTemplate.getForEntity(getUrl, CustomerResponse.class);
        return customer.getBody();
    }

    public CustomerResponse lookupCustomerFirstName(final String firstName) {
        final String getUrl = String.format(base_customers_url + "/firstName/%s", firstName);
        ResponseEntity<CustomerResponse> customer = restTemplate.getForEntity(getUrl, CustomerResponse.class);
        return customer.getBody();
    }

    public CustomerResponse lookupCustomerLastName(final String lastName) {
        final String getUrl = String.format(base_customers_url + "/lastName/%s", lastName);
        ResponseEntity<CustomerResponse> customer = restTemplate.getForEntity(getUrl, CustomerResponse.class);
        return customer.getBody();
    }

    public CustomerResponse lookupCustomerNdi(final String ndi) {
        final String getUrl = String.format(base_customers_url + "/ndi/%s", ndi);
        ResponseEntity<CustomerResponse> customer = restTemplate.getForEntity(getUrl, CustomerResponse.class);
        return customer.getBody();
    }

    public CustomerResponse addCustomer(final CustomerData protoCustomer) {
        final String postUrl = base_customers_url;
        ResponseEntity<CustomerResponse> customer = restTemplate.postForEntity(postUrl, protoCustomer, CustomerResponse.class);
        return customer.getBody();
    }

    public void deleteCustomer(final long customerId) {
        final String deleteUrl = String.format(base_customers_url + "/%d", customerId);
        restTemplate.delete(deleteUrl);
    }

    // -------

    public boolean setCustomerImage(final long customerId, final String filePath) {
        final String postUrl = String.format(base_customers_image_url, customerId);
        final File fileImage = new File(filePath);
        final String name = fileImage.getName();
        final String data = UtilBase64Image.INSTANCE.encoder(filePath);

        final ImageData image = new ImageData(name, data);

        ResponseEntity<ImageData> postResponse = restTemplate.postForEntity(postUrl, image, ImageData.class);
        return postResponse.getStatusCode().is2xxSuccessful();
    }

    public ImageData getCustomerImage(final long customerId) {
        final String getUrl = String.format(base_customers_image_url, customerId);
        return restTemplate.getForEntity(getUrl, ImageData.class).getBody();
    }

}
