package io.theam.api

import org.pac4j.core.authorization.authorizer.RequireAnyRoleAuthorizer
import org.pac4j.core.client.Clients
import org.pac4j.core.config.Config
import org.pac4j.core.config.ConfigFactory
import org.pac4j.core.profile.CommonProfile
import org.pac4j.http.client.direct.HeaderClient
import org.pac4j.jwt.config.signature.SecretSignatureConfiguration
import org.pac4j.jwt.credentials.authenticator.JwtAuthenticator
import org.pac4j.jwt.profile.JwtGenerator
import org.pac4j.sparkjava.DefaultHttpActionAdapter
import spark.Route


val secret = "12345678901234567890123456789012"
val signatureConfiguration = SecretSignatureConfiguration(secret)

class TheamConfigFactory: ConfigFactory {

    override fun build(vararg parameters: Any?): Config {

        val headerClient = HeaderClient(
                "Authorization",
                "Bearer ",
                JwtAuthenticator(signatureConfiguration))

        val clients = Clients(headerClient)
        clients.defaultClient = headerClient

        val config = Config(clients)
        config.addAuthorizer("admin", RequireAnyRoleAuthorizer<CommonProfile>("ROLE_ADMIN"))
        config.httpActionAdapter = DefaultHttpActionAdapter()
        return config
    }

}

// util to generate a token to test
fun calculateToken() : Route = Route { _,_ ->
    val generator = JwtGenerator<CommonProfile>(signatureConfiguration)
    val profile = CommonProfile()
    profile.addAttributes(
            mapOf(
                    "email" to "pepe.potamo@canaria.zoo",
                    "first_name" to "Pepe",
                    "family_name" to "James",
                    "display_name" to "Pepe J.",
                    "username" to "pepejames")
    )
    profile.addRole("ROLE_ADMIN")
    hashMapOf( "token" to generator.generate(profile))
}
