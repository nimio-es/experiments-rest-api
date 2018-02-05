package io.theam.client.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Option;
import io.theam.client.service.RestClient;
import io.theam.model.Customer;

@Command(name = "show", description = "Shows the info of a customer")
public class GetCustomerInfoCommand extends BaseCommand {

    @Option(name = "--id", description = "Customer identity to load")
    public Long customerId = -1L;

    @Override
    protected boolean validate() {
        super.validate();
        if(customerId <  0L) {
            System.out.println("The customer id is necessary.");
            return false;
        }
        return true;
    }

    @Override
    protected void doRun() {

        Customer customer = new RestClient(username, password).getCustomer(customerId);

        try {
            System.out.println(
                    new ObjectMapper()
                            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                            .configure(SerializationFeature.INDENT_OUTPUT, true)
                            .writeValueAsString(customer));
        } catch (JsonProcessingException e) {
            new RuntimeException(e);
        }
    }
}
