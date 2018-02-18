package io.theam.client.commands.customers

import com.fasterxml.jackson.core.JsonProcessingException
import com.github.rvesse.airline.annotations.Command
import com.github.rvesse.airline.annotations.Option
import io.theam.client.commands.BaseCommand
import io.theam.client.service.CustomersRestClient
import io.theam.model.api.CustomerResponseImageData
import io.theam.util.UtilBase64Image
import org.apache.commons.lang3.StringUtils
import java.awt.Desktop
import java.io.File
import java.io.IOException

@Command(name = "show", description = "Shows the info of a customer")
class ShowCustomerInfoCommand : BaseCommand() {

    @Option(name = ["--id"], description = "Customer identity to search")
    var customerId: Long? = -1L

    @Option(name = ["--first-name", "-fn"], description = "Customer first name to search")
    var firstName: String? = null

    @Option(name = ["--last-name", "-ln"], description = "Customer last name to search")
    var lastName: String? = null

    @Option(name = ["--ndi"], description = "Customer last name to search")
    var ndi: String? = null

    @Option(name = ["--image-file"], description = "Include in the info the image and save in the path")
    var imagePath: String? = null

    @Option(name = ["--show"], description = "Opens default application to show the image")
    var showImage = false

    override fun validate(): Boolean {
        var result = super.validate()
        if (customerId!! <= 0 && StringUtils.isEmpty(firstName) && StringUtils.isEmpty(lastName) && StringUtils.isEmpty(ndi)) {
            System.err.println("Or customer id, or first name, or last name or ndi have to be defined to complete de search")
            result = false
        }
        return result
    }

    override fun doRun() {

        val restClient = CustomersRestClient(username, password)
        val customer = if (customerId!! > 0)
            restClient.getCustomer(customerId!!, !StringUtils.isEmpty(imagePath))
        else if (!StringUtils.isEmpty(firstName))
            restClient.lookupCustomerFirstName(firstName!!)
        else if (!StringUtils.isEmpty(lastName))
            restClient.lookupCustomerLastName(lastName!!)
        else
            restClient.lookupCustomerNdi(ndi!!)

        try {
            println(pretty_print_json.writeValueAsString(customer))

            if (customer.image is CustomerResponseImageData.Image) {
                val imageData = customer.image as CustomerResponseImageData.Image
                UtilBase64Image.decoder(imageData.imageData.fileData, imagePath!!)

                if (showImage) {
                    try {
                        Desktop.getDesktop().open(File(imagePath!!))
                    } catch (e: IOException) {
                        throw RuntimeException(e)
                    }

                }
            }

        } catch (e: JsonProcessingException) {
            RuntimeException(e)
        }

    }
}
