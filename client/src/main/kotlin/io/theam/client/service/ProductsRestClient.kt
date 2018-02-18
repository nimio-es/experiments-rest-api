package io.theam.client.service

import io.theam.model.api.ProductData
import io.theam.model.api.ProductResponse

class ProductsRestClient(username: String, password: String) : BaseRestClient(username, password)  {

    companion object {
        private const val base_products_url = base_url + "/products"
    }

    // ----

    val allProducts: Collection<ProductData>
        get() {
            val products = restTemplate.getForEntity(
                    base_products_url,
                    Collection::class.java as Class<Collection<ProductData>>)

            return products.body
        }

    fun saveProduct(ref: String, name: String, price: Double) : ProductResponse =
            restTemplate.postForEntity(
                base_products_url,
                ProductData(ref, name, price),
                ProductResponse::class.java).body
}