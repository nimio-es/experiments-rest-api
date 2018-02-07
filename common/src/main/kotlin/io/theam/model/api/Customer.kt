package io.theam.model.api

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.node.TextNode

/**
 * Basic data
 */
data class CustomerData(val firstName:String, val lastName:String, val ndi:String)
data class ImageData(val fileName: String, val fileData: String)

/**
 * Response data
 */
@JsonSerialize(using = CustomerResponseImageDataSerializer::class)
@JsonDeserialize(using = CustomerResponseImageDataDeserializer::class)
sealed class CustomerResponseImageData {
    object NoImage: CustomerResponseImageData()
    object HasImage: CustomerResponseImageData()
    data class Image(val imageData: ImageData): CustomerResponseImageData()
}

data class CustomerResponse(
        val customerId: Long,
        val customer: CustomerData,
        val image: CustomerResponseImageData)


open class CustomerResponseImageDataSerializer : JsonSerializer<CustomerResponseImageData>() {

    override fun serialize(value: CustomerResponseImageData?, jgen: JsonGenerator?, provider: SerializerProvider?) {

        when(value!!) {
            is CustomerResponseImageData.NoImage -> jgen?.writeString("without_image")
            is CustomerResponseImageData.HasImage -> jgen?.writeString("has_image")
            is CustomerResponseImageData.Image -> {
                val v = value as CustomerResponseImageData.Image
                jgen?.writeObject(v.imageData)
            }
        }
    }
}

open class CustomerResponseImageDataDeserializer : JsonDeserializer<CustomerResponseImageData>() {
    override fun deserialize(parser: JsonParser?, context: DeserializationContext?): CustomerResponseImageData {
        val node = parser!!.codec!!.readTree<JsonNode>(parser)

        return when(node) {
            is TextNode -> when(node.asText()) {
                "without_image" -> CustomerResponseImageData.NoImage
                else -> CustomerResponseImageData.HasImage
            }
            else -> CustomerResponseImageData.Image(ImageData(node.get("fileName").asText(), node.get("fileData").asText()))
        }
    }
}
