package io.theam.client

import com.github.rvesse.airline.Cli
import com.github.rvesse.airline.help.Help
import io.theam.client.commands.*

object ClientApp {

    @JvmStatic
    fun main(args: Array<String>) {

        val builder = Cli.builder<Runnable>("theam-cli")
                .withDescription("Theam example operations")
                .withDefaultCommand(Help::class.java)

        builder.withGroup("customers")
                .withDescription("Operations with customers")
                .withCommand(ShowCustomerInfoCommand::class.java)
                .withCommand(AddCustomerCommand::class.java)
                .withCommand(DeleteCustomerCommand::class.java)
                .withDefaultCommand(CustomerListCommand::class.java)
                .withSubGroup("image")
                .withCommand(SetCustomerImageCommand::class.java)
                .withDefaultCommand(GetCustomerImageCommand::class.java)
                .build()

        builder.withGroup("images")
                .withDescription("Operations with images")
                .withCommand(GetImageCommand::class.java)
                .withDefaultCommand(GetImageListCommand::class.java)
                .build()

        val client = builder.build()

        client.parse(*args).run()
    }
}
