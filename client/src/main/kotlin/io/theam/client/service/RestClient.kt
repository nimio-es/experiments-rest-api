package io.theam.client.service

import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.theam.model.api.CustomerData
import io.theam.model.api.CustomerResponse
import io.theam.model.api.ImageData
import io.theam.model.api.ImageResponse
import io.theam.util.UtilBase64Image
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext
import org.springframework.security.oauth2.client.OAuth2RestTemplate
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails
import java.io.File
import java.util.*

class RestClient(username: String, password: String) {

    private val restTemplate: OAuth2RestTemplate

    val customers: Collection<CustomerResponse>
        get() {
            val getUrl = base_customers_url

            val customers = restTemplate.getForEntity(
                    getUrl,
                    Collection::class.java as Class<Collection<CustomerResponse>>)

            return customers.body
        }

    // -------

    val allImages: Collection<ImageResponse>
        get() {
            val getUrl = base_images_url
            return restTemplate.getForEntity(getUrl, Collection::class.java as Class<Collection<ImageResponse>>).body
        }

    init {

        val resourceDetails = ResourceOwnerPasswordResourceDetails()
        resourceDetails.username = username
        resourceDetails.password = password
        resourceDetails.accessTokenUri = base_url + "/oauth/token"
        resourceDetails.clientId = "theam"
        resourceDetails.clientSecret = "secret"
        resourceDetails.grantType = "password"
        resourceDetails.scope = Arrays.asList("read", "write")

        val clientContext = DefaultOAuth2ClientContext()

        this.restTemplate = OAuth2RestTemplate(resourceDetails, clientContext)
        val mpcv = MappingJackson2HttpMessageConverter()
        mpcv.objectMapper.registerKotlinModule()
        restTemplate.messageConverters = Arrays.asList<HttpMessageConverter<*>>(mpcv)
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

        private val base_url = "http://localhost:8080"
        private val base_customers_url = base_url + "/customers"
        private val base_customers_image_url = base_customers_url + "/%d/image"
        private val base_images_url = base_url + "/images"
    }

}
