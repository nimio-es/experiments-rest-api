package io.theam

import com.google.gson.Gson
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import com.xenomachina.argparser.mainBody
import io.theam.api.*
import org.pac4j.sparkjava.SecurityFilter
import spark.Filter
import spark.ResponseTransformer
import spark.Spark.*

fun main(args: Array<String>) {

    mainBody {

        ApiArgs(ArgParser(args)).run {

            val config = TheamConfigFactory().build()
            val secFilter = SecurityFilter(config, "HeaderClient", "admin")

            port(port)

            // security filter
            before("customers", secFilter)
            before("customers/*", secFilter)

            // customers API
            path("customers") {

                get("", CustomersAPI.listCustomers, ResponseTransformer { Gson().toJson(it) })         // list of all customers
                post("", CustomersAPI.addCustomer, ResponseTransformer { Gson().toJson(it) })          // adds a new customer
                put("", CustomersAPI.editCustomer, ResponseTransformer { Gson().toJson(it) })          // modify a customer
                delete("/:id", CustomersAPI.deleteCustomer, ResponseTransformer { Gson().toJson(it) }) // removes a customer
                options("/:id", CustomersAPI.existCustomer)               // checks the existence of a customer

                path("/:id") {
                    // the customer
                    get("", CustomersAPI.getCustomer, ResponseTransformer { Gson().toJson(it) })       // get customer by

                    // the image
                    path("/image") {
                        get("") { _, _ -> "{ \"message:\": \"TO BE DONE\" }" }
                        post("") { _, _ -> "{ \"message:\": \"TO BE DONE\" }" }
                        put("") { _, _ -> "{ \"message:\": \"TO BE DONE\" }" }
                        delete("") { _, _ -> "{ \"message:\": \"TO BE DONE\" }" }
                    }

                    // purchases
                    path("/purchases") {
                        get("") { _, _ -> "{ \"message:\": \"TO BE DONE\" }" }
                        get("/:purchaseId") { _, _ -> "{ \"message:\": \"TO BE DONE\" }" }
                        post("") { _, _ -> "{ \"message:\": \"TO BE DONE\" }" }
                        put("/:purchaseId") { _, _ -> "{ \"message:\": \"TO BE DONE\" }" }
                        delete("/:purchaseId") { _, _ -> "{ \"message:\": \"TO BE DONE\" }" }
                    }
                }
            }

            // -- shutdown
            get("shutdown", ShutdownAPI.shutdownServer)


            // -- util token generator
            get("admintoken", calculateAdminToken(), ResponseTransformer { Gson().toJson(it) })
            get("nobodytoken", calculateNobodyToke(), ResponseTransformer { Gson().toJson(it) })

            after(Filter { _, a -> a.type("application/json") }) // all is json in this API
            after(
                    "/*",
                    { _, a -> a.header("What-is-my-purpose", "This is part of a technical coding interview for the Theam.io company") }
            )

        }
    }
}


class ApiArgs(parser: ArgParser) {
    val port by parser.storing("-p", "--port", help = "Port to use") { toInt() }.default(8080)
}

