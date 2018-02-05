package io.theam.client;

import com.github.rvesse.airline.Cli;
import com.github.rvesse.airline.builder.CliBuilder;
import com.github.rvesse.airline.help.Help;
import io.theam.client.commands.CustomerListCommand;
import io.theam.client.commands.GetCustomerInfoCommand;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClientApp {

    public static void main(String[] args){

        CliBuilder<Runnable> builder = Cli.<Runnable>builder("theam")
                .withDescription("Theam example operations")
                .withCommand(Help.class)
                .withDefaultCommand(Help.class);

        builder.withGroup("customers")
                .withDescription("Operations with customers")
                .withCommands(GetCustomerInfoCommand.class)
                .withDefaultCommand(CustomerListCommand.class);

        Cli<Runnable> client = builder.build();

        client.parse(args).run();
    }
}
