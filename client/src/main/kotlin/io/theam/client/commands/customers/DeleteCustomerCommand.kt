package io.theam.client.commands.customers

import com.github.rvesse.airline.annotations.Command
import io.theam.client.commands.BaseCommandWithId
import io.theam.client.service.delete
import io.theam.client.service.withUrl

@Command(name = "delete", description = "Delete a customer")
class DeleteCustomerCommand : BaseCommandWithId() {

    override fun doRun() =
            restClient withUrl
                    "$host/customers/${customerId!!}" delete
                    {println("Customer with id: ${customerId!!} was removed")}
}
