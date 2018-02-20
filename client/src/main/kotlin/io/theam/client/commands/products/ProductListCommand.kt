package io.theam.client.commands.products

import com.github.rvesse.airline.annotations.Command
import io.theam.client.commands.BaseCommand
import io.theam.client.commands.printWith
import io.theam.client.service.bodyOf
import io.theam.client.service.getEntity
import io.theam.client.service.withUrl
import io.theam.model.api.ProductResponse
import org.springframework.core.ParameterizedTypeReference

@Command(name = "list", description = "Get the list of all products")
class ProductListCommand : BaseCommand() {

    private fun getAllProducts() : Collection<ProductResponse> =
            bodyOf (restClient withUrl "$host/products" getEntity
                    object : ParameterizedTypeReference<Collection<ProductResponse>>() {})

    override fun doRun() =
            getAllProducts() printWith  pretty_print_json
}
