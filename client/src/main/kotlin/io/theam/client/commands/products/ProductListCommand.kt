package io.theam.client.commands.products

import com.fasterxml.jackson.core.JsonProcessingException
import com.github.rvesse.airline.annotations.Command
import io.theam.client.commands.BaseCommand
import io.theam.client.service.ProductsRestClient

@Command(name = "list", description = "Get the list of all products")
class ProductListCommand : BaseCommand() {
    override fun doRun() {

        val images = ProductsRestClient(username, password).allProducts
        try {
            println(pretty_print_json.writeValueAsString(images))
        } catch (e: JsonProcessingException) {
            throw RuntimeException(e)
        }

    }
}
