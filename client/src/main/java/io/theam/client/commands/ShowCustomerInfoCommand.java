package io.theam.client.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Option;
import io.theam.client.service.RestClient;
import io.theam.model.Customer;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

@Command(name = "show", description = "Shows the info of a customer")
public class ShowCustomerInfoCommand extends BaseCommand {

    @Option(name = "--id", description = "Customer identity to search")
    public Long customerId = -1L;

    @Option(name = {"--first-name", "-fn"}, description = "Customer first name to search")
    public String firstName;

    @Option(name = {"--last-name", "-ln"}, description = "Customer last name to search")
    public String lastName;

    @Option(name = "--ndi", description = "Customer last name to search")
    public String ndi;


    @Override
    protected boolean validate() {
        boolean result = super.validate();
        if(customerId <= 0 && StringUtils.isEmpty(firstName) && StringUtils.isEmpty(lastName) && StringUtils.isEmpty(ndi)) {
            System.err.println("Or customer id, or first name, or last name or ndi have to be defined to complete de search");
            result = false;
        }
        return result;
    }

    @Override
    protected void doRun() {

        final RestClient restClient = new RestClient(username, password);
        Customer customer = customerId > 0
                ? restClient.getCustomer(customerId)
                : !StringUtils.isEmpty(firstName)
                    ? restClient.lookupCustomerFirstName(firstName)
                    : !StringUtils.isEmpty(lastName)
                        ? restClient.lookupCustomerLastName(lastName)
                        : restClient.lookupCustomerNdi(ndi);

        try {
            System.out.println(pretty_print_json.writeValueAsString(customer));
        } catch (JsonProcessingException e) {
            new RuntimeException(e);
        }
    }
}
