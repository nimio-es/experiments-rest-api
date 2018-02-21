package es.nimio.exercise.client.commands.customers

import com.github.rvesse.airline.annotations.Command
import es.nimio.exercise.client.commands.BaseCommand
import es.nimio.exercise.client.service.bodyOf
import es.nimio.exercise.client.service.getEntity
import es.nimio.exercise.client.service.withUrl
import es.nimio.exercise.model.api.CustomerResponse
import org.springframework.core.ParameterizedTypeReference

@Command(name = "list", description = "Get the list of all customers")
class CustomerListCommand : BaseCommand() {

    private val customers: Collection<CustomerResponse>
        get() = bodyOf( restClient withUrl
                "$host/customers" getEntity
                    object : ParameterizedTypeReference<Collection<CustomerResponse>>() {})

    public override fun doRun() =
            customers.let { listOf(
                    pretty_print_json.writeValueAsString(it),
                    "------------",
                    "Number of customers: ${it.size}")
                    .forEach {println(it)} }
}
