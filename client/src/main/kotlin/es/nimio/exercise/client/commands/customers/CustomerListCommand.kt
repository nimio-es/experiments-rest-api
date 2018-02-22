package es.nimio.exercise.client.commands.customers

import com.github.rvesse.airline.annotations.Command
import com.github.rvesse.airline.annotations.Option
import com.github.rvesse.airline.annotations.OptionType
import es.nimio.exercise.client.commands.BaseCommand
import es.nimio.exercise.client.service.bodyOf
import es.nimio.exercise.client.service.getEntity
import es.nimio.exercise.client.service.withUrl
import es.nimio.exercise.model.api.CustomerResponse
import org.springframework.core.ParameterizedTypeReference

@Command(name = "list", description = "Get the list of all customers")
class CustomerListCommand : BaseCommand() {

    @Option(type = OptionType.COMMAND,
            name = [ "--first-name", "-fn" ],
            description = "For searching using first name")
    var byFirstName: String? = null

    @Option(type = OptionType.COMMAND,
            name = [ "--last-name", "-ln" ],
            description = "For searching using first name")
    var byLastName: String? = null

    @Option(type = OptionType.COMMAND,
            name = [ "--ndi" ],
            description = "For searching using ndi")
    var byNdi: String? = null

    val isSearch get() =
        (byFirstName ?: "").isNotBlank()
                || (byLastName ?: "").isNotBlank()
                || (byNdi ?: "").isNotBlank()

    val baseUrl get() = "$host/customers" + (if(isSearch) "/search" else "")

    val urlGet get() =
        baseUrl +
                when {
                    (byFirstName ?: "").isNotBlank() -> "/firstName/$byFirstName"
                    (byLastName ?: "").isNotBlank() -> "/lastName/$byLastName"
                    (byNdi ?: "").isNotBlank() -> "/ndi/$byNdi"
                    else -> ""
                }

    private val customers: Collection<CustomerResponse>
        get() = bodyOf( restClient withUrl
                urlGet getEntity
                    object : ParameterizedTypeReference<Collection<CustomerResponse>>() {})

    public override fun doRun() =
            customers.let { listOf(
                    pretty_print_json.writeValueAsString(it),
                    "------------",
                    "Number of customers: ${it.size}")
                    .forEach {println(it)} }
}
