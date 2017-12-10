package io.theam.api;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.*;

public final class Start {

    private static final Logger LOG = LoggerFactory.getLogger(Start.class);

    private static final Gson GSON = new Gson();

    public static void main(String[] args) {

        before("customers", OAuthCheckerFilter.oauthCheckerFilter);           // checks oauth authorization
        before("customers/*", OAuthCheckerFilter.oauthCheckerFilter);

        // customers API
        path("customers", () -> {

            get("", CustomersAPI.listCustomers, GSON::toJson);         // list of all customers
            post("", CustomersAPI.addCustomer, GSON::toJson);          // adds a new customer
            put("", CustomersAPI.editCustomer, GSON::toJson);          // modify a customer
            delete("/:id", CustomersAPI.deleteCustomer, GSON::toJson); // removes a customer
            options("/:id", CustomersAPI.existCustomer);               // checks the existence of a customer

            path("/:id", () -> {
                // the customer
                get("", CustomersAPI.getCustomer, GSON::toJson);       // get customer by

                // the image
                path("/image", () -> {
                    get("", (q, a) -> "{ \"message:\": \"TO BE DONE\" }");
                    post("", (q, a) -> "{ \"message:\": \"TO BE DONE\" }");
                    put("", (q, a) -> "{ \"message:\": \"TO BE DONE\" }");
                    delete("", (q, a) -> "{ \"message:\": \"TO BE DONE\" }");
                });

                // purchases
                path("/purchases", () -> {
                    get("", (q, a) -> "{ \"message:\": \"TO BE DONE\" }");
                    get("/:purchaseId", (q, a) -> "{ \"message:\": \"TO BE DONE\" }");
                    post("", (q, a) -> "{ \"message:\": \"TO BE DONE\" }");
                    put("/:purchaseId", (q, a) -> "{ \"message:\": \"TO BE DONE\" }");
                    delete("/:purchaseId", (q, a) -> "{ \"message:\": \"TO BE DONE\" }");
                });
            });
        });

        // -- shutdown
        get("shutdown", ShutdownAPI.shutdownServer);

        after((q, a) -> a.type("application/json"));  // all is json in this API
        after(
                "/*",
                (q, a) -> a.header("What-is-my-purpose", "This is part of a technical coding interview for the Theam.io company")
        );
    }
}
