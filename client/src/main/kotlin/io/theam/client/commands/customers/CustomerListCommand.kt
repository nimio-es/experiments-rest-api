package io.theam.client.commands.customers

import com.fasterxml.jackson.core.JsonProcessingException
import com.github.rvesse.airline.annotations.Command
import io.theam.client.commands.BaseCommand
import io.theam.client.service.RestClient
import io.theam.model.api.CustomerResponse

@Command(name = "list", description = "Get the list of all customers")
class CustomerListCommand : BaseCommand() {

    public override fun doRun() {

        var customers: Collection<CustomerResponse>? = null
        customers = RestClient(username, password).customers

        try {
            println(pretty_print_json.writeValueAsString(customers))
        } catch (e: JsonProcessingException) {
            RuntimeException(e)
        }

        println("---------")
        println("Number of customers: " + Integer.toString(customers.size))
    }
}
