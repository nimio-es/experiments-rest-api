package io.theam.client.commands.customers

import com.fasterxml.jackson.core.JsonProcessingException
import com.github.rvesse.airline.annotations.Command
import com.github.rvesse.airline.annotations.Option
import io.theam.client.commands.BaseCommand
import io.theam.client.service.RestClient
import io.theam.model.api.CustomerData
import org.apache.commons.lang3.StringUtils

@Command(name = "add", description = "Adds a new customer")
class AddCustomerCommand : BaseCommand() {

    @Option(name = ["--first-name", "-fn"], description = "The name of the customer")
    var firstName: String? = null

    @Option(name = ["--last-name", "-ln"], description = "The last name of the customer")
    var lastName: String? = null

    @Option(name = ["--ndi"], description = "The national document of identity")
    var ndi: String? = null

    override fun validate(): Boolean {
        var result = super.validate()

        if (StringUtils.isEmpty(firstName) || StringUtils.isEmpty(lastName) || StringUtils.isEmpty(ndi)) {
            println("You have to set First Name, Last Name and/or NDI")
            result = false
        }

        return result
    }

    override fun doRun() {

        val protoCustomer = CustomerData(firstName!!, lastName!!, ndi!!)

        val savedCustomer = RestClient(username, password).addCustomer(protoCustomer)

        try {
            println(pretty_print_json.writeValueAsString(savedCustomer))
        } catch (e: JsonProcessingException) {
            RuntimeException(e)
        }

    }
}
