package io.theam.model.api

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.node.TextNode

/*
 * Basic data
 */
data class CustomerData(val firstName:String, val lastName:String, val ndi:String)
data class ImageData(val fileName: String, val fileData: String)
data class UserData(val userName: String, val authorities: List<String>)

/*
 * Response data
 */

/**
 * Information about the image of the customer
 */
@JsonSerialize(using = CustomerResponseImageDataSerializer::class)
@JsonDeserialize(using = CustomerResponseImageDataDeserializer::class)
sealed class CustomerResponseImageData {
    object NoImage: CustomerResponseImageData()
    data class HasImage(val imageId: Long): CustomerResponseImageData()
    data class Image(val imageData: ImageData): CustomerResponseImageData()
}

/**
 * Customer API response
 */
data class CustomerResponse(
        val customerId: Long,
        val customer: CustomerData,
        val image: CustomerResponseImageData)

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "type")
sealed class ImageResponse {
    data class OnlyImage(val imageData: ImageData): ImageResponse()
    data class ImageWithCustomer(val imageData: ImageData, val customer: CustomerData) : ImageResponse()
}


/*
 * SERIALIZATION
 */

open class CustomerResponseImageDataSerializer : JsonSerializer<CustomerResponseImageData>() {

    override fun serialize(value: CustomerResponseImageData?, jgen: JsonGenerator?, provider: SerializerProvider?) {

        when(value!!) {
            is CustomerResponseImageData.NoImage -> jgen?.writeString("without_image")
            is CustomerResponseImageData.HasImage -> {
                val v = value as CustomerResponseImageData.HasImage
                jgen?.writeStartObject()
                jgen?.writeNumberField("imageId", v.imageId)
                jgen?.writeEndObject()
            }
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
            is TextNode -> CustomerResponseImageData.NoImage
            else ->
                if (node.hasNonNull("imageId")) CustomerResponseImageData.HasImage(node.get("imageId").asLong())
                else CustomerResponseImageData.Image(ImageData(node.get("fileName").asText(), node.get("fileData").asText()))
        }
    }
}
