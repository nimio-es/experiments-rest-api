package es.nimio.exercise.client.service

import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext
import org.springframework.security.oauth2.client.OAuth2RestTemplate
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails
import org.springframework.web.client.RestTemplate
import java.util.*

class RestClient(username: String, password: String, urlBase: String) {

    val restTemplate: OAuth2RestTemplate

    init {
        val resourceDetails = ResourceOwnerPasswordResourceDetails()
        resourceDetails.username = username
        resourceDetails.password = password
        resourceDetails.accessTokenUri = urlBase + "/oauth/token"
        resourceDetails.clientId = "nimio"
        resourceDetails.clientSecret = "nivi-is-beautiful"
        resourceDetails.grantType = "password"
        resourceDetails.scope = Arrays.asList("read", "write")

        val clientContext = DefaultOAuth2ClientContext()

        this.restTemplate = OAuth2RestTemplate(resourceDetails, clientContext)
        val mpcv = MappingJackson2HttpMessageConverter()
        mpcv.objectMapper.registerKotlinModule()
        restTemplate.messageConverters = Arrays.asList<HttpMessageConverter<*>>(mpcv)
    }
}

/*
 * DISCLAIMER: Probably this isn't the best way of do calls using RestTemplate. To be honest, it's
 * probably a crap solution. But it's here because all this project is built to satisfy my appetite
 * of experiment with different ways of make things.
 * In the future it's really probably I'll change all and make call in the old way (dot notation
 * for invoking methods).
 */

interface WithState // allows define infix for multiple cases
class RestClientWithUrl(val cl: RestTemplate, val url: String) : WithState
class RestClientWithUrlAndData<T>(val cl: RestTemplate, val url: String, val data:T) : WithState

infix fun RestClient.withUrl(url: String) = RestClientWithUrl(this.restTemplate, url)
infix fun <T> RestClientWithUrl.getObject(cz: Class<T>):T = this.cl.getForObject(this.url, cz)
infix fun <T> RestClientWithUrl.getEntity(pt: ParameterizedTypeReference<T>): ResponseEntity<T> =
        cl.exchange(url, HttpMethod.GET, null, pt)
infix fun <T> RestClientWithUrl.getEntity(cz: Class<T>): ResponseEntity<T> = cl.getForEntity(url, cz)
infix fun <T> RestClientWithUrl.postObject(cz: Class<T>):T = cl.postForObject(url, null, cz)
infix fun RestClientWithUrl.delete(postAction: () -> Unit) = cl.delete(url).let { postAction() }

infix fun <T> RestClientWithUrl.withData(data: T) =
        RestClientWithUrlAndData(cl, url, data)

infix fun <T> WithState.postEntity(cz: Class<T>): ResponseEntity<T> =
        when {
            this is RestClientWithUrl -> cl.postForEntity(url, null, cz)
            this is RestClientWithUrlAndData<*> -> cl.postForEntity(url, data, cz)
            else -> throw RuntimeException("No post allowed")
        }

/**
 * Using "any" allows start the sequence using implicit this in most of the cases
 */
infix fun <T> Any.bodyOf(re: ResponseEntity<T>) = re.body