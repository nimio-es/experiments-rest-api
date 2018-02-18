package io.theam.client.commands.products

import com.github.rvesse.airline.annotations.Command
import com.github.rvesse.airline.annotations.Option
import io.theam.client.commands.BaseCommand
import io.theam.client.service.ProductsRestClient

@Command(name = "create", description = "Creates a new product")
class CreateProductCommand : BaseCommand() {

    @Option(name = ["--reference", "-r"], description = "Sets the reference of the product")
    var ref: String = ""

    @Option(name = ["--name", "-n"], description = "Sets the name of the product")
    var name: String = ""

    @Option(name = ["--common-price", "-pr"], description = "Sets the common price of the product")
    var commonPrice: Double = 0.0

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

    override fun doRun() {
        println(pretty_print_json.writeValueAsString(
                ProductsRestClient(username, password)
                        .saveProduct(ref, name, commonPrice)))
    }
}