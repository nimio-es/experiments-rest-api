package io.theam.client.commands.products

import com.github.rvesse.airline.annotations.Command
import io.theam.client.commands.BaseCommand
import io.theam.client.commands.printWith
import io.theam.client.service.getAllProducts

@Command(name = "list", description = "Get the list of all products")
class ProductListCommand : BaseCommand() {
    override fun doRun() =
            restClient().getAllProducts() printWith pretty_print_json
}
