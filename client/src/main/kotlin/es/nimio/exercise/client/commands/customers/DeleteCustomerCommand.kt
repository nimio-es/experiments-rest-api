package es.nimio.exercise.client.commands.customers

import com.github.rvesse.airline.annotations.Command
import es.nimio.exercise.client.commands.BaseCommandWithId
import es.nimio.exercise.client.service.delete
import es.nimio.exercise.client.service.withUrl

@Command(name = "delete", description = "Delete a customer")
class DeleteCustomerCommand : BaseCommandWithId() {

    override fun doRun() =
            restClient withUrl
                    "$host/customers/${customerId!!}" delete
                    {println("Customer with id: ${customerId!!} was removed")}
}
