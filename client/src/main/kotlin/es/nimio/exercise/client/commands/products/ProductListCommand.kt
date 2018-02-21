package es.nimio.exercise.client.commands.products

import com.github.rvesse.airline.annotations.Command
import es.nimio.exercise.client.commands.BaseCommand
import es.nimio.exercise.client.commands.printWith
import es.nimio.exercise.client.service.bodyOf
import es.nimio.exercise.client.service.getEntity
import es.nimio.exercise.client.service.withUrl
import es.nimio.exercise.model.api.ProductResponse
import org.springframework.core.ParameterizedTypeReference

@Command(name = "list", description = "Get the list of all products")
class ProductListCommand : BaseCommand() {

    private fun getAllProducts() : Collection<ProductResponse> =
            bodyOf (restClient withUrl "$host/products" getEntity
                    object : ParameterizedTypeReference<Collection<ProductResponse>>() {})

    override fun doRun() =
            getAllProducts() printWith  pretty_print_json
}
