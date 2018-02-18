package io.theam.client.commands.customers

import com.github.rvesse.airline.annotations.Command
import io.theam.client.commands.BaseCommandWithId
import io.theam.client.service.CustomersRestClient

@Command(name = "delete", description = "Delete a customer")
class DeleteCustomerCommand : BaseCommandWithId() {

    override fun doRun() {
        println("Removing customer with id: " + java.lang.Long.toString(customerId!!))
        CustomersRestClient(username, password).deleteCustomer(customerId!!)
    }
}
