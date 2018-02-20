package io.theam.client.service

import io.theam.model.api.ProductData
import io.theam.model.api.ProductResponse

/**
 * Gets all products
 */
fun RestClient.getAllProducts() : Collection<ProductData> =
        "${this.urlBase}/products".let {
            getUrl -> this.restTemplate.getForEntity(
                getUrl,
                Collection::class.java as Class<Collection<ProductData>>)
                .body
        }

/**
 * Saves a new product
 */
fun RestClient.saveNewProduct(ref: String, name: String, price: Double) : ProductResponse =
        "${this.urlBase}/products".let {
            postUrl -> restTemplate.postForEntity(
                postUrl,
                ProductData(ref, name, price),
                ProductResponse::class.java)
                .body
        }
