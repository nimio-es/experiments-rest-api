package io.theam.client.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.rvesse.airline.annotations.Command;
import io.theam.client.service.RestClient;
import io.theam.model.Customer;

@Command(name = "show", description = "Shows the info of a customer")
public class ShowCustomerInfoCommand extends BaseCommandWithId {

    @Override
    protected void doRun() {

        Customer customer = new RestClient(username, password).getCustomer(customerId);

        try {
            System.out.println(pretty_print_json.writeValueAsString(customer));
        } catch (JsonProcessingException e) {
            new RuntimeException(e);
        }
    }
}
