package es.nimio.exercise.client.commands.customers

import com.github.rvesse.airline.annotations.Command
import com.github.rvesse.airline.annotations.Option
import es.nimio.exercise.client.commands.BaseCommand
import es.nimio.exercise.client.commands.printWith
import es.nimio.exercise.client.service.bodyOf
import es.nimio.exercise.client.service.postEntity
import es.nimio.exercise.client.service.withData
import es.nimio.exercise.client.service.withUrl
import es.nimio.exercise.model.api.CustomerData
import es.nimio.exercise.model.api.CustomerResponse
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

    private fun addCustomer(): CustomerResponse =
            bodyOf(restClient withUrl
                    "$host/customers" withData
                    CustomerData(firstName!!, lastName!!, ndi!!) postEntity
                    CustomerResponse::class.java)

    override fun doRun() = addCustomer() printWith pretty_print_json
}
