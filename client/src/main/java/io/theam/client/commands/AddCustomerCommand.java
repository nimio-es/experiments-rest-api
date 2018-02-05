package io.theam.client.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Option;
import io.theam.client.service.RestClient;
import io.theam.model.Customer;
import org.apache.commons.lang3.StringUtils;

@Command(name = "add", description = "Adds a new customer")
public class AddCustomerCommand extends BaseCommand {

    @Option(name= {"--first-name", "-fn"}, description = "The name of the customer")
    public String firstName;

    @Option(name = {"--last-name", "-ln"}, description = "The last name of the customer")
    public String lastName;

    @Option(name = "--ndi", description = "The national document of identity")
    public String ndi;

    @Override
    protected boolean validate() {
        boolean result = super.validate();

        if (StringUtils.isEmpty(firstName) || StringUtils.isEmpty(lastName) || StringUtils.isEmpty(ndi)) {
            System.out.println("You have to set First Name, Last Name and/or NDI");
            result = false;
        }

        return result;
    }

    @Override
    protected void doRun() {

        final Customer protoCustomer = new Customer();
        protoCustomer.setFirstName(firstName);
        protoCustomer.setLastName(lastName);
        protoCustomer.setNdi(ndi);

        final Customer savedCustomer = new RestClient(username, password).addCustomer(protoCustomer);

        try {
            System.out.println(pretty_print_json.writeValueAsString(savedCustomer));
        } catch (JsonProcessingException e) {
            new RuntimeException(e);
        }

    }
}
