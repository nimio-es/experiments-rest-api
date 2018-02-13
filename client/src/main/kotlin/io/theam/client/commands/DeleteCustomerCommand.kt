package io.theam.client.commands

import com.github.rvesse.airline.annotations.Command
import io.theam.client.service.RestClient

@Command(name = "delete", description = "Delete a customer")
class DeleteCustomerCommand : BaseCommandWithId() {

    override fun doRun() {
        println("Removing customer with id: " + java.lang.Long.toString(customerId!!))
        RestClient(username, password).deleteCustomer(customerId!!)
    }
}
