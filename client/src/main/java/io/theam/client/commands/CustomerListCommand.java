package io.theam.client.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.rvesse.airline.annotations.Command;
import io.theam.client.service.RestClient;
import io.theam.model.Customer;

import java.util.Collection;

@Command(name = "list", description = "Get the list of all customers")
public class CustomerListCommand extends BaseCommand {

    @Override
    public void doRun() {

        Collection<Customer> customers = null;
        customers = new RestClient(username, password).getCustomers();

        try {
            System.out.println(pretty_print_json.writeValueAsString(customers));
        } catch (JsonProcessingException e) {
            new RuntimeException(e);
        }
        System.out.println("---------");
        System.out.println("Number of customers: " + Integer.toString(customers.size()));
    }
}
