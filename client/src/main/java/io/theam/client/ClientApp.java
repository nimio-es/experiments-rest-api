package io.theam.client;

import com.github.rvesse.airline.Cli;
import com.github.rvesse.airline.builder.CliBuilder;
import com.github.rvesse.airline.help.Help;
import io.theam.client.commands.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClientApp {

    public static void main(String[] args){

        CliBuilder<Runnable> builder = Cli.<Runnable>builder("theam-cli")
                .withDescription("Theam example operations")
                .withCommand(Help.class)
                .withDefaultCommand(Help.class);

        builder.withGroup("customers")
                .withDescription("Operations with customers")
                .withCommand(ShowCustomerInfoCommand.class)
                .withCommand(AddCustomerCommand.class)
                .withCommand(DeleteCustomerCommand.class)
                .withDefaultCommand(CustomerListCommand.class)
                .withSubGroup("image")
                .withCommand(SetCustomerImageCommand.class)
                .withDefaultCommand(GetCustomerImageCommand.class);

        Cli<Runnable> client = builder.build();

        client.parse(args).run();
    }
}
