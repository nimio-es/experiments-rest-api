package es.nimio.exercise.util

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

fun String.encodeFromPath() : String =
        File(this).let { file ->
            FileInputStream(file).use { imageInFile ->
                // Reading a Image file from file system
                val imageData = ByteArray(file.length().toInt())
                imageInFile.read(imageData)
                return Base64.getEncoder().encodeToString(imageData)
            }
        }

infix fun String.decodeToFile(pathFile: String) =
        FileOutputStream(pathFile).use { imageOutFile ->
            // Converting a Base64 String into Image byte array
            val imageByteArray = Base64.getDecoder().decode(this)
            imageOutFile.write(imageByteArray)
        }