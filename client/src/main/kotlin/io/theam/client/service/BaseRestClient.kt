package io.theam.client.service

import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext
import org.springframework.security.oauth2.client.OAuth2RestTemplate
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails
import java.util.*

abstract class BaseRestClient(username: String, password: String) {

    internal val restTemplate: OAuth2RestTemplate

    companion object {
        internal const val base_url = "http://localhost:8080"
    }

    // -------

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


}