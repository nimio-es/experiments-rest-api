package io.theam.client.commands.customers

import com.github.rvesse.airline.annotations.Command
import com.github.rvesse.airline.annotations.Option
import io.theam.client.commands.BaseCommandWithId
import io.theam.client.service.bodyOf
import io.theam.client.service.getEntity
import io.theam.client.service.withUrl
import io.theam.model.api.ImageData
import io.theam.util.decodeToFile
import java.awt.Desktop
import java.io.File

@Command(name = "get", description = "Gets/downloads the image of a customer")
class GetCustomerImageCommand : BaseCommandWithId() {

    @Option(name = ["--show"], description = "Opens default application to show the image")
    var showImage = false

    @Option(name = ["--file", "-f"], description = "The path to store the downloaded image")
    var imagePath: String? = null

    private fun getCustomerImage(): ImageData =
            bodyOf(restClient withUrl
                    "$host/customers/${customerId!!}/image" getEntity ImageData::class.java)

    override fun doRun() =
            getCustomerImage().let { image ->
               if ((imagePath ?: "").isBlank()) {
                    println("Image downloaded but not stored locally (no image path defined)")
                    println(image.toString())
                } else {
                    image.fileData decodeToFile imagePath!!
                    if (showImage) Desktop.getDesktop().open(File(imagePath!!))
                }
            }
}
