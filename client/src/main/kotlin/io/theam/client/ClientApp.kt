package io.theam.client

import com.github.rvesse.airline.Cli
import com.github.rvesse.airline.help.Help
import io.theam.client.commands.customers.*
import io.theam.client.commands.images.GetImageCommand
import io.theam.client.commands.images.ImageListCommand
import io.theam.client.commands.products.CreateProductCommand
import io.theam.client.commands.products.ProductListCommand
import io.theam.client.commands.purchases.PurchaseListCommand
import io.theam.client.commands.purchases.RegisterNewPurchaseCommand
import io.theam.client.commands.users.CreateUserCommand
import io.theam.client.commands.users.UserListCommand

object ClientApp {

    @JvmStatic
    fun main(args: Array<String>) {

        val builder = Cli.builder<Runnable>("theam-cli")
                .withDescription("Theam example operations")
                .withDefaultCommand(Help::class.java)

        builder.withGroup("users")
                .withDescription("Operations to manage users")
                .withCommand(CreateUserCommand::class.java)
                .withDefaultCommand(UserListCommand::class.java)
                .build()

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
                .withDefaultCommand(ImageListCommand::class.java)
                .build()

        builder.withGroup("products")
                .withDescription("Operations with products")
                .withCommand(CreateProductCommand::class.java)
                .withDefaultCommand(ProductListCommand::class.java)
                .build()

        builder.withGroup("purchases")
                .withDescription("Operations with purchases")
                .withCommand(RegisterNewPurchaseCommand::class.java)
                .withDefaultCommand(PurchaseListCommand::class.java)
                .build()

        val client = builder.build()

        client.parse(*args).run()
    }
}
