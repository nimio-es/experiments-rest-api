package io.theam.util

import org.slf4j.LoggerFactory
import java.io.*
import java.util.*

object UtilBase64Image {

    private val logger = LoggerFactory.getLogger(UtilBase64Image::class.java)

    fun encoder(imagePath: String): String? {
        val file = File(imagePath)
        try {
            FileInputStream(file).use { imageInFile ->
                // Reading a Image file from file system
                var base64Image = ""
                val imageData = ByteArray(file.length().toInt())
                imageInFile.read(imageData)
                base64Image = Base64.getEncoder().encodeToString(imageData)
                return base64Image
            }
        } catch (e: FileNotFoundException) {
            println("Image not found" + e)
        } catch (ioe: IOException) {
            println("Exception while reading the Image " + ioe)
        }

        return null
    }

    fun decoder(base64Image: String, pathFile: String) {
        logger.info("Write to file: {}", pathFile)
        try {
            FileOutputStream(pathFile).use { imageOutFile ->
                // Converting a Base64 String into Image byte array
                val imageByteArray = Base64.getDecoder().decode(base64Image)
                imageOutFile.write(imageByteArray)
            }
        } catch (e: FileNotFoundException) {
            println("Image not found" + e)
        } catch (ioe: IOException) {
            println("Exception while reading the Image " + ioe)
        }

        logger.info("File saved!")
    }
}