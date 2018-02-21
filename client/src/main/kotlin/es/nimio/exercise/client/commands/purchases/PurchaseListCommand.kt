package es.nimio.exercise.client.commands.purchases

import com.github.rvesse.airline.annotations.Command
import com.github.rvesse.airline.annotations.Option
import es.nimio.exercise.client.commands.BaseCommand
import es.nimio.exercise.client.commands.printWith
import es.nimio.exercise.client.service.bodyOf
import es.nimio.exercise.client.service.getEntity
import es.nimio.exercise.client.service.withUrl
import es.nimio.exercise.model.api.PurchaseData
import org.springframework.core.ParameterizedTypeReference

@Command(name = "list", description = "Get the list of purchase of a customer, of a product or of the intersection of both")
class PurchaseListCommand  : BaseCommand() {

    @Option(name = ["--customer-id"], description = "Customer identity")
    var customerId: Long? = null

    @Option(name = ["--product-id"], description = "Customer identity")
    var productId: Long? = null


    private fun calculateUrl() =
            "$host/purchases" +
                    (if(customerId != null) "/ofCustomer/$customerId" else "") +
                    (if(productId != null) "/ofProduct/$productId" else "")

    private fun listOfPurchases() : Collection<PurchaseData> =
            bodyOf (restClient withUrl
                    calculateUrl() getEntity
                    object : ParameterizedTypeReference<Collection<PurchaseData>>() {})

    override fun validate(): Boolean {
        var result = super.validate()

        if(customerId==null && productId == null) {
            System.err.println("Or customer id or product id is required")
            result = false
        }

        return result
    }

    override fun doRun() = listOfPurchases() printWith  pretty_print_json
}