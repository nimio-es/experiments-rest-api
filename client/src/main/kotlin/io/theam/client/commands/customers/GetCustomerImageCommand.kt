package io.theam.client.commands.customers

import com.github.rvesse.airline.annotations.Command
import com.github.rvesse.airline.annotations.Option
import io.theam.client.commands.BaseCommandWithId
import io.theam.client.service.CustomersRestClient
import io.theam.util.UtilBase64Image
import org.apache.commons.lang3.StringUtils
import java.awt.Desktop
import java.io.File
import java.io.IOException

@Command(name = "get", description = "Gets/downloads the image of a customer")
class GetCustomerImageCommand : BaseCommandWithId() {

    @Option(name = ["--show"], description = "Opens default application to show the image")
    var showImage = false

    @Option(name = ["--file", "-f"], description = "The path to store the downloaded image")
    var imagePath: String? = null

    override fun doRun() {

        val image = CustomersRestClient(username, password).getCustomerImage(customerId!!)

        if (StringUtils.isEmpty(imagePath)) {
            println("Image downloaded but not stored locally (no image path defined)")
            println(image.toString())
        } else {
            UtilBase64Image.decoder(image.fileData, imagePath!!)

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
