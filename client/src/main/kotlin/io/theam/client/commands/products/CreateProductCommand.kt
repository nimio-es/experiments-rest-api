package io.theam.client.commands.products

import com.github.rvesse.airline.annotations.Command
import com.github.rvesse.airline.annotations.Option
import io.theam.client.commands.BaseCommand
import io.theam.client.commands.printWith
import io.theam.client.service.bodyOf
import io.theam.client.service.postEntity
import io.theam.client.service.withData
import io.theam.client.service.withUrl
import io.theam.model.api.ProductData
import io.theam.model.api.ProductResponse

@Command(name = "create", description = "Creates a new product")
class CreateProductCommand : BaseCommand() {

    @Option(name = ["--reference", "-r"], description = "Sets the reference of the product")
    var ref: String = ""

    @Option(name = ["--name", "-n"], description = "Sets the name of the product")
    var name: String = ""

    @Option(name = ["--common-price", "-pr"], description = "Sets the common price of the product")
    var commonPrice: Double = 0.0

    private fun saveNewProduct(): ProductResponse =
            bodyOf (restClient withUrl
                    "$host/products" withData
                    ProductData(ref, name, commonPrice) postEntity
                    ProductResponse::class.java)


    override fun validate(): Boolean {
        var result = super.validate()
        if(ref.isBlank()) {
            println("The reference is necessary")
            result = false
        }
        if(name.isBlank()) {
            println("The name is necessary")
            result = true
        }
        return result
    }

    override fun doRun() =
        saveNewProduct() printWith pretty_print_json
}