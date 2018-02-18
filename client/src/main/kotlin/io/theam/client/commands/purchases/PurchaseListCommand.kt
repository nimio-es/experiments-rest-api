package io.theam.client.commands.purchases

import com.github.rvesse.airline.annotations.Command
import com.github.rvesse.airline.annotations.Option
import io.theam.client.commands.BaseCommand
import io.theam.client.service.PurchasesRestClient

@Command(name = "list", description = "Get the list of purchase of a customer, of a product or of the intersection of both")
class PurchaseListCommand  : BaseCommand() {

    @Option(name = ["--customer-id"], description = "Customer identity")
    var customerId: Long? = null

    @Option(name = ["--product-id"], description = "Customer identity")
    var productId: Long? = null

    override fun validate(): Boolean {
        var result = super.validate()

        if(customerId==null && productId == null) {
            System.err.println("Or customer id or product id is required")
            result = false
        }

        return result
    }

    override fun doRun() =
        println(pretty_print_json.writeValueAsString(
                PurchasesRestClient(username, password)
                        .listOfPurchases(customerId, productId)))
}