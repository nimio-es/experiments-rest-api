package io.theam.client.commands.customers

import com.github.rvesse.airline.annotations.Command
import com.github.rvesse.airline.annotations.Option
import io.theam.client.commands.BaseCommandWithId
import io.theam.client.service.RestClient
import org.apache.commons.lang3.StringUtils

@Command(name = "set", description = "Sets the image of a customer")
class SetCustomerImageCommand : BaseCommandWithId() {

    @Option(name = ["--file", "-f"], description = "File with the image to upload")
    var imagePath: String? = null

    override fun validate(): Boolean {
        var result = super.validate()
        if (StringUtils.isEmpty(imagePath)) {
            println("You have to define the path where load the image file")
            result = false
        }
        return result
    }

    override fun doRun() {
        println("Associate the image '" + imagePath + "' to customer with id: " + java.lang.Long.toString(customerId!!))
        RestClient(username, password).setCustomerImage(customerId!!, imagePath!!)
    }
}
