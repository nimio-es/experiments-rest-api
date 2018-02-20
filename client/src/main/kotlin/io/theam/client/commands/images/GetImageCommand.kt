package io.theam.client.commands.images

import com.github.rvesse.airline.annotations.Command
import com.github.rvesse.airline.annotations.Option
import io.theam.client.commands.BaseCommand
import io.theam.client.service.bodyOf
import io.theam.client.service.getEntity
import io.theam.client.service.withUrl
import io.theam.model.api.ImageResponse
import io.theam.util.decodeToFile
import java.awt.Desktop
import java.io.File
import java.io.IOException

@Command(name = "get", description = "Gets/downloads the image of a customer")
class GetImageCommand : BaseCommand() {

    @Option(name = ["--id"], description = "Image identity to handle")
    var imageId: Long? = -1L

    @Option(name = ["--show"], description = "Opens default application to show the image")
    var showImage = false

    @Option(name = ["--file", "-f"], description = "The path to store the downloaded image")
    var imagePath: String? = null

    override fun validate(): Boolean {
        var result = super.validate()
        if (this.imageId!! < 0L) {
            println("The image id is necessary.")
            result = false
        }
        return result
    }

    private fun getImage(): ImageResponse =
            bodyOf(restClient withUrl
                    "$host/images/$imageId!!" getEntity
                    ImageResponse::class.java)

    override fun doRun() = getImage().let {
        image ->
        if((imagePath ?: "").isBlank()) {
            println("Image downloaded but not stored locally (no image path defined)")
            println(image.toString())
        } else {
            val imageData = (image as? ImageResponse.OnlyImage)?.imageData
                    ?: (image as ImageResponse.ImageWithCustomer).imageData

            imageData.fileData decodeToFile imagePath!!

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
