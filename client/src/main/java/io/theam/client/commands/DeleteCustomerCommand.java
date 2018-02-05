package io.theam.client.commands;

import com.github.rvesse.airline.annotations.Command;
import io.theam.client.service.RestClient;

@Command(name = "delete", description = "Delete a customer")
public class DeleteCustomerCommand extends BaseCommandWithId {

    @Override
    protected void doRun() {
        System.out.println("Removing customer with id: " + Long.toString(customerId));
        new RestClient(username, password).deleteCustomer(customerId);
    }
}
