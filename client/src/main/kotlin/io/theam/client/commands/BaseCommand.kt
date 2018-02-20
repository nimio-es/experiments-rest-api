package io.theam.client.commands

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.github.rvesse.airline.annotations.Option
import io.theam.client.service.RestClient
import org.apache.commons.lang3.StringUtils
import java.util.*

abstract class BaseCommand : Runnable {

    companion object {
        @JvmStatic
        var pretty_print_json : ObjectMapper = ObjectMapper()
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .configure(SerializationFeature.INDENT_OUTPUT, true)
    }

    @Option(name = ["--username", "-u"], description = "Define the user name to access")
    var username: String = Optional.ofNullable(System.getenv("THEAM_USERNAME")).orElse("")

    @Option(name = ["--password", "-p"], description = "Define the user password to access")
    var password: String = Optional.ofNullable(System.getenv("THEAM_PASSWORD")).orElse("")

    protected open fun validate(): Boolean {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            println("***********************************************************************")
            println("Username and/or Password are required parameters. Ask for help command!")
            println("***********************************************************************")
            return false
        }
        return true
    }

    /**
     * Template method pattern
     */
    protected abstract fun doRun()

    override fun run() {
        if (validate()) {
            doRun()
        }
    }

    // ----

    /**
     * Util method that gets the username and password and creates a
     * new RestClient utility and avoid that each command make the same call
     */
    protected fun restClient(): RestClient = RestClient(username, password)

}
