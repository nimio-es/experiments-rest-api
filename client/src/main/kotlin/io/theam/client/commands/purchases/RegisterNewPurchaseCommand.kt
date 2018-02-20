package io.theam.client.commands.purchases

import com.github.rvesse.airline.annotations.Command
import com.github.rvesse.airline.annotations.Option
import com.github.rvesse.airline.annotations.OptionType
import io.theam.client.commands.BaseCommand
import io.theam.client.commands.printWith
import io.theam.client.service.*
import io.theam.model.api.PurchaseData
import java.util.*

@Command(name = "add", description = "Adds a purchase to collection")
class RegisterNewPurchaseCommand: BaseCommand() {

    @Option(type = OptionType.COMMAND, name = [ "--customer-id", "-ci" ], description = "The customer's Id")
    val customerId: Long? = null

    @Option(type = OptionType.COMMAND, name = [ "--product-id", "-pi" ], description = "The product's Id")
    val productId: Long? = null

    @Option(type = OptionType.COMMAND, name = [ "--num-of-items", "-ni" ], description = "The number of items sold")
    val numOfItems: Int? = null

    @Option(type = OptionType.COMMAND, name = [ "--unit-price", "-up" ], description = "The price of each item")
    val priceOfEachItem: Double? = null

    private fun newPurchase(): PurchaseData =
            bodyOf(restClient withUrl
                    "$host/purchases" withData
                    PurchaseData(
                            Date(),
                            customerId!!,
                            productId!!,
                            numOfItems!!,
                            priceOfEachItem!!) postEntity
                    PurchaseData::class.java)

    override fun doRun() =
            newPurchase() printWith pretty_print_json
}