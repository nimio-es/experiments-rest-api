package io.theam.api

import com.google.gson.Gson
import io.theam.model.Customer
import org.apache.http.HttpResponse
import org.apache.http.client.methods.HttpDelete
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClientBuilder
import org.junit.AfterClass
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import spark.Spark
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

import io.theam.main
import java.net.ServerSocket

class InsertAndListOfCustomersTest {

    companion object {

        @JvmStatic
        var urlBase: String = ""

        @JvmStatic
        @BeforeClass
        fun beforeClass() {
            val freePort = findFreePort()
            main(arrayOf("--port", freePort.toString()))
            urlBase = "http://localhost:$freePort"

            Spark.awaitInitialization()
        }

        @JvmStatic
        @AfterClass
        fun afterClass() {
            Spark.stop()
        }

        @JvmStatic
        private fun findFreePort() : Int = ServerSocket(0).use { socket -> return socket.localPort }
    }

    @Test
    fun exceptionWhenAccessWithoutToken() {
        val request = HttpGet(urlBase + "/customers")
        val response = createTestResponse(executeRequest(request))
        Assert.assertEquals(401, response.status)
    }

    @Test
    fun forbiddenWhenAccessWithNobodyToken() {
        val response = getRequest(urlBase + "/customers", getNobodyToken())
        Assert.assertEquals(404, response.status)
    }

    @Test
    fun createUserAndGet() {

        // insert and check response
        val res = postRequest("/customers", "{ \"firstName\": \"Lukas\", \"lastName\": \"Grijanderl\", \"nid\": \"666666P\" }")
        assertEquals(200, res.status)

        val customer = Gson().fromJson<Customer>(res.body, Customer::class.java)
        assertNotNull(customer)

        // now gets directly
        val customerGet = Gson().fromJson<Customer>(
                getRequest("/customers/" + customer.id).body,   // using the stored id
                Customer::class.java)
        assertNotNull(customerGet)
        assertEquals(customer, customerGet)
    }

    @Test
    fun createUserAndDelete() {

        // insert and check response
        val res = postRequest("/customers", "{ \"firstName\": \"Lukas\", \"lastName\": \"Grijanderl\", \"nid\": \"666666P\" }")
        assertEquals(200, res.status)

        val customer = Gson().fromJson<Customer>(res.body, Customer::class.java)
        assertNotNull(customer)

        // and now delete it
        val resDelete = deleteRequest("/customers/" + customer.id)
        assertEquals(200, resDelete.status)

        // and try to load (and using java classes fails)
        val resGet = getRequest("/customers/" + customer.id)
        assertEquals(400, resGet.status)
    }
}

private fun getToken() =
        Gson().fromJson<TokenMessage>(
                getBody(
                        executeRequest(
                                HttpGet(InsertAndListOfCustomersTest.urlBase + "/admintoken")
                        ).entity.content),
                TokenMessage::class.java).token

private fun getNobodyToken() =
        Gson().fromJson<TokenMessage>(
                getBody(
                        executeRequest(
                                HttpGet(InsertAndListOfCustomersTest.urlBase + "/nobodytoken")
                        ).entity.content),
                TokenMessage::class.java).token

private fun getRequest(path: String) = getRequest(path, getToken())

private fun getRequest(path: String, token: String): TestResponse {
    val request = HttpGet(InsertAndListOfCustomersTest.urlBase + path)
    request.setHeader("Authorization", "Bearer " + token)
    return createTestResponse(executeRequest(request))
}

private fun postRequest(path: String, body: String = ""): TestResponse {

    val request = HttpPost(InsertAndListOfCustomersTest.urlBase + path)
    request.setHeader("Authorization", "Bearer " + getToken())
    if (!body.isEmpty()) {
        val input = StringEntity(body)
        input.setContentType("application/json")
        request.entity = input
    }
    return createTestResponse(executeRequest(request))
}

private fun deleteRequest(path: String): TestResponse {
    val request = HttpDelete(InsertAndListOfCustomersTest.urlBase + path)
    request.setHeader("Authorization", "Bearer " + getToken())
    return createTestResponse(executeRequest(request))
}

private fun executeRequest(request: HttpUriRequest): HttpResponse = HttpClientBuilder.create().build().execute(request)

private fun createTestResponse(httpResponse: HttpResponse): TestResponse = TestResponse(
        httpResponse.statusLine.statusCode,
        getBody(httpResponse.entity.content))

private fun getBody(inputStream: InputStream): String = BufferedReader(InputStreamReader(inputStream)).readText()

private data class TestResponse(val status: Int, val body: String)
