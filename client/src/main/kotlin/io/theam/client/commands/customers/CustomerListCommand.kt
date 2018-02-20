package io.theam.client.commands.customers

import com.github.rvesse.airline.annotations.Command
import io.theam.client.commands.BaseCommand
import io.theam.client.service.bodyOf
import io.theam.client.service.getEntity
import io.theam.client.service.withUrl
import io.theam.model.api.CustomerResponse
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
