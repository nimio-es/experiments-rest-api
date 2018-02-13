package io.theam.client.service

import io.theam.model.api.CustomerData
import io.theam.model.api.CustomerResponse
import io.theam.model.api.ImageData
import io.theam.model.api.ImageResponse
import io.theam.util.UtilBase64Image
import java.io.File

class RestClient(username: String, password: String) : BaseRestClient(username, password) {

    // ----

    val customers: Collection<CustomerResponse>
        get() {
            val getUrl = base_customers_url

            val customers = restTemplate.getForEntity(
                    getUrl,
                    Collection::class.java as Class<Collection<CustomerResponse>>)

            return customers.body
        }

    fun getCustomer(customerId: Long, includeImage: Boolean): CustomerResponse {
        val getUrl = String.format(base_customers_url + "/%d?includeImage=%s", customerId, java.lang.Boolean.toString(includeImage).toLowerCase())
        val customer = restTemplate.getForEntity(getUrl, CustomerResponse::class.java)
        return customer.body
    }

    fun lookupCustomerFirstName(firstName: String): CustomerResponse {
        val getUrl = String.format(base_customers_url + "/firstName/%s", firstName)
        val customer = restTemplate.getForEntity(getUrl, CustomerResponse::class.java)
        return customer.body
    }

    fun lookupCustomerLastName(lastName: String): CustomerResponse {
        val getUrl = String.format(base_customers_url + "/lastName/%s", lastName)
        val customer = restTemplate.getForEntity(getUrl, CustomerResponse::class.java)
        return customer.body
    }

    fun lookupCustomerNdi(ndi: String): CustomerResponse {
        val getUrl = String.format(base_customers_url + "/ndi/%s", ndi)
        val customer = restTemplate.getForEntity(getUrl, CustomerResponse::class.java)
        return customer.body
    }

    fun addCustomer(protoCustomer: CustomerData): CustomerResponse {
        val postUrl = base_customers_url
        val customer = restTemplate.postForEntity(postUrl, protoCustomer, CustomerResponse::class.java)
        return customer.body
    }

    fun deleteCustomer(customerId: Long) {
        val deleteUrl = String.format(base_customers_url + "/%d", customerId)
        restTemplate.delete(deleteUrl)
    }

    // -------

    val allImages: Collection<ImageResponse>
        get() {
            val getUrl = base_images_url
            return restTemplate.getForEntity(getUrl, Collection::class.java as Class<Collection<ImageResponse>>).body
        }

    fun setCustomerImage(customerId: Long, filePath: String): Boolean {
        val postUrl = String.format(base_customers_image_url, customerId)
        val fileImage = File(filePath)
        val name = fileImage.name
        val data = UtilBase64Image.encoder(filePath)

        val image = ImageData(name, data!!)

        val postResponse = restTemplate.postForEntity(postUrl, image, ImageData::class.java)
        return postResponse.statusCode.is2xxSuccessful
    }

    fun getCustomerImage(customerId: Long): ImageData {
        val getUrl = String.format(base_customers_image_url, customerId)
        return restTemplate.getForEntity(getUrl, ImageData::class.java).body
    }

    fun getImage(imageId: Long): ImageResponse {
        val getUrl = String.format(base_images_url + "/%d", imageId)
        return restTemplate.getForEntity(getUrl, ImageResponse::class.java).body
    }

    companion object {

        private const val base_url = "http://localhost:8080"
        private const val base_customers_url = base_url + "/customers"
        private const val base_customers_image_url = base_customers_url + "/%d/image"
        private const val base_images_url = base_url + "/images"
        private const val base_users_url = base_url + "/users"
    }

}
