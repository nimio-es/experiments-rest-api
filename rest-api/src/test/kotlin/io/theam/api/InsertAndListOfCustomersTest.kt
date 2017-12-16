package io.theam.api

import com.google.gson.Gson
import io.theam.model.Customer
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import spark.Spark
import spark.utils.IOUtils
import java.io.IOException
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


class InsertAndListOfCustomersTest {

    companion object {

        @JvmStatic @BeforeClass
        fun beforeClass() {
            Start.main(null)
            Spark.awaitInitialization()
        }

        @JvmStatic @AfterClass
        fun afterClass() {
            Spark.stop()
        }
    }

    @Test
    fun createUserAndGet() {

        // insert and check response
        val res = request("POST", "/customers", "{ \"firstName\": \"Lukas\", \"lastName\": \"Grijanderl\", \"nid\": \"666666P\" }")
        assertEquals(200, res.status)

        val customer = Gson().fromJson<Customer>(res.body, Customer::class.java)
        assertNotNull(customer)

        // now gets directly
        val customerGet = Gson().fromJson<Customer>(
                request("GET", "/customers/" + customer.id).body,   // using the stored id
                Customer::class.java)
        assertNotNull(customerGet)
        assertEquals(customer, customerGet)
    }

    @Test
    fun createUserAndDelete() {

        // insert and check response
        val res = request("POST", "/customers", "{ \"firstName\": \"Lukas\", \"lastName\": \"Grijanderl\", \"nid\": \"666666P\" }")
        assertEquals(200, res.status)

        val customer = Gson().fromJson<Customer>(res.body, Customer::class.java)
        assertNotNull(customer)

        // and now delete it
        val resDelete = request("DELETE", "/customers/" + customer.id)
        assertEquals(200, resDelete.status)

        // and try to load (and using java classes fails)
        val resGet = request("GET", "/customers/" + customer.id)
        assertEquals(400, resGet.status)
    }
}

private fun request(method: String, path: String, body: String = ""): TestResponse {
    return try {
        val url = URL("http://localhost" + path)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = method
        connection.setRequestProperty("Accept", "application/json");
        connection.doOutput = true

        // writes the body
        if(!body.isNullOrEmpty()) {
            connection.doInput = true
            connection.setRequestProperty("Content-Type", "application/json")
            val wr = OutputStreamWriter(connection.outputStream)
            wr.write(body)
            wr.flush()
        } else {}

        connection.connect()
        val body = IOUtils.toString(connection.inputStream)
        TestResponse(connection.responseCode, body)
    } catch (e: IOException) {
        // because HttpURLConnection throws an exception if the response status code is different of 200!!!
        TestResponse(400, "")
    }
}

private data class TestResponse(val status: Int, val body: String)