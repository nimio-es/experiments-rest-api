package io.theam.model.api

data class CustomerData(val firstName:String, val lastName:String, val ndi:String)

data class CustomerResponse(val customerId: Long, val customer: CustomerData)

data class ImageData(val fileName: String, val fileData: String)

