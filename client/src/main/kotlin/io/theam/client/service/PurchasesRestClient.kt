package io.theam.client.service

import io.theam.model.api.PurchaseData
import java.util.*

class PurchasesRestClient(username: String, password: String) : BaseRestClient(username, password)  {

    companion object {
        private const val base_purchases_url = base_url + "/purchases"
    }

    // ----

    fun listOfPurchases(customerId: Long?, productId: Long?) : Collection<PurchaseData> =
        restTemplate
                .getForEntity(
                        base_purchases_url +
                        (if(customerId != null) "/ofCustomer/$customerId" else "") +
                        (if(productId != null) "/ofProduct/$productId" else ""),
                        Collection::class.java as Class<Collection<PurchaseData>>)
                .body

    fun newPurchase(customerId: Long, productId: Long, numberOfItems: Int, itemPrice: Double): PurchaseData =
        restTemplate
                .postForEntity(
                        base_purchases_url,
                        PurchaseData(
                                Date(),
                                customerId,
                                productId,
                                numberOfItems,
                                itemPrice),
                        PurchaseData::class.java)
                .body


}