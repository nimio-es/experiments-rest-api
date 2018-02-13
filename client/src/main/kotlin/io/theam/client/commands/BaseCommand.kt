package io.theam.client.commands

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.github.rvesse.airline.annotations.Option
import org.apache.commons.lang3.StringUtils

abstract class BaseCommand : Runnable {

    companion object {
        @JvmStatic
        protected val pretty_print_json : ObjectMapper = ObjectMapper()
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .configure(SerializationFeature.INDENT_OUTPUT, true)
    }

    @Option(name = ["--username", "-u"], description = "Define the user to access")
    var username: String = System.getenv("THEAM_USERNAME")

    @Option(name = ["--password", "-p"], description = "Define the user to access")
    var password: String = System.getenv("THEAM_PASSWORD")

    protected open fun validate(): Boolean {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            println("***********************************************************************")
            println("Username and/or Password are required parameters. Ask for help command!")
            println("***********************************************************************")
            return false
        }
        return true
    }

    protected abstract fun doRun()

    override fun run() {
        if (validate()) {
            doRun()
        }
    }
}
