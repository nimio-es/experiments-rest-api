package io.theam.client.service

import io.theam.model.api.PurchaseData

class PurchasesRestClient(username: String, password: String) : BaseRestClient(username, password)  {

    companion object {
        private const val base_purchases_url = base_url + "/purchases"
    }

    // ----

    fun listOfPurchases(customerId: Long?, productId: Long?) : Collection<PurchaseData> {

        val finalGetUrl = base_purchases_url +
                (if(customerId != null) "/ofCustomer/$customerId" else "") +
                (if(productId != null) "/ofProduct/$productId" else "")

        return restTemplate
                .getForEntity(finalGetUrl, Collection::class.java as Class<Collection<PurchaseData>>)
                .body
    }
}