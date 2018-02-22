package es.nimio.exercise.client.commands.customers

import com.github.rvesse.airline.annotations.Command
import com.github.rvesse.airline.annotations.Option
import com.github.rvesse.airline.annotations.OptionType
import es.nimio.exercise.client.commands.BaseCommand
import es.nimio.exercise.client.commands.printWith
import es.nimio.exercise.client.service.bodyOf
import es.nimio.exercise.client.service.getEntity
import es.nimio.exercise.client.service.withUrl
import es.nimio.exercise.model.api.CustomerResponse
import es.nimio.exercise.model.api.CustomerResponseImageData
import es.nimio.exercise.util.decodeToFile
import org.apache.commons.lang3.StringUtils
import java.awt.Desktop
import java.io.File
import java.io.IOException

@Command(name = "show", description = "Shows the info of a customer")
class ShowCustomerInfoCommand : BaseCommand() {

    @Option(type = OptionType.COMMAND, name = ["--id"], description = "Customer identity to search")
    var customerId: Long? = null

    @Option(type = OptionType.COMMAND, name = ["--first-name", "-fn"], description = "Customer first name to search")
    var firstName: String? = null

    @Option(type = OptionType.COMMAND, name = ["--last-name", "-ln"], description = "Customer last name to search")
    var lastName: String? = null

    @Option(type = OptionType.COMMAND, name = ["--ndi"], description = "Customer last name to search")
    var ndi: String? = null

    @Option(type = OptionType.COMMAND, name = ["--image-file"], description = "Include in the info the image and save in the path")
    var imagePath: String? = null

    @Option(type = OptionType.COMMAND, name = ["--show"], description = "Opens default application to show the image")
    var showImage = false

    @Option(type = OptionType.COMMAND, name = ["--include-purchases"], description = "Include the purchases made by this customer")
    var includePurchases : Boolean = false

    override fun validate(): Boolean {
        var result = super.validate()
        if (customerId!! <= 0 && StringUtils.isEmpty(firstName) && StringUtils.isEmpty(lastName) && StringUtils.isEmpty(ndi)) {
            System.err.println("Or customer id, or first name, or last name or ndi have to be defined to complete de search")
            result = false
        }
        return result
    }

    private val baseUrl get() = "$host/customers"

    private fun getCustomer() =
            bodyOf(restClient withUrl
                    "$baseUrl/${customerId!!}?includeImage=$showImage&includePurchases=$includePurchases" getEntity
                    CustomerResponse::class.java)

    private fun lookupCustomerFirstName(): CustomerResponse =
            bodyOf(restClient withUrl
                    "$baseUrl/firstName/${firstName!!}" getEntity
                    CustomerResponse::class.java)

    private fun lookupCustomerLastName(): CustomerResponse =
            bodyOf(restClient withUrl
                    "$baseUrl/lastName/$lastName" getEntity
                    CustomerResponse::class.java)

    private fun lookupCustomerNdi(): CustomerResponse =
            bodyOf(restClient withUrl
                    "$baseUrl/ndi/$ndi" getEntity
                    CustomerResponse::class.java)

    override fun doRun() =
            when {
                customerId != null -> getCustomer()
                (firstName ?: "").isNotBlank() -> lookupCustomerFirstName()
                (lastName ?: "").isNotBlank() -> lookupCustomerLastName()
                else -> lookupCustomerNdi()
            }.let { customer ->
                customer printWith pretty_print_json

                if (customer.image is CustomerResponseImageData.Image) {
                    val imageData = customer.image as CustomerResponseImageData.Image
                    imageData.imageData.fileData decodeToFile imagePath!!

                    if (showImage) {
                        try {
                            Desktop.getDesktop().open(File(imagePath!!))
                        } catch (e: IOException) {
                            throw RuntimeException(e)
                        }

                    }
                }
            }
}
