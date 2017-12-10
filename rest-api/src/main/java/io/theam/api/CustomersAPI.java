package io.theam.api;

import com.google.gson.Gson;
import io.theam.model.Customer;
import io.theam.services.CustomersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Route;

import static spark.Spark.halt;

final class CustomersAPI {

    private static final Logger LOG = LoggerFactory.getLogger(CustomersAPI.class);

    private static final CustomersService CUSTOMERS_SERVICE = new CustomersService();

    /**
     * Returns all customers
     */
    static final Route listCustomers = (q, a) -> {
        LOG.info("List of all customers with query params: {}", q.queryParams());
        return CUSTOMERS_SERVICE.allCustomers(q.queryParams().contains("sorted"));
    };

    /**
     * Looks for a customer with a concrete Id
     */
    static final Route getCustomer = (q, a) -> {
        final String id = q.params("id");
        LOG.info("Get customer by id: {}", id);
        try {
            return CUSTOMERS_SERVICE.getCustomer(id);
        } catch (CustomersService.CustomersDoesNotExistException ex) {
            LOG.error("The requested customer doesn't exist");
            return halt(400, ex.getMessage());
        }
    };

    /**
     * Creates a new customer. But for this it's necessary to use a partial representation
     * without all the additional data.
     */
    static final Route addCustomer = (q, a) -> {
        LOG.info("Creates a new customer with info: {}", q.body());
        final CustomersService.PartialCreationCustomerData partialCustomer =
                new Gson().fromJson(q.body(), CustomersService.PartialCreationCustomerData.class);
        try {
            return CUSTOMERS_SERVICE.addCustomer(partialCustomer);
        } catch (Customer.InvalidCustomerDataException ex) {
            return halt(422, String.format("Unprocessable entity because: %s", ex.getMessage()));
        }
    };

    /**
     * Modify a customer.
     */
    static final Route editCustomer = (q, a) -> {
        LOG.info("Modify existing customer with: {}", q.body());
        try {
            final Customer editCustomer = Customer.fromJson(q.body());
            return CUSTOMERS_SERVICE.editCustomer(editCustomer);
        } catch (Customer.InvalidCustomerDataException exd) {
            return halt(422, String.format("Unprocessable entity because: %s", exd.getMessage()));
        } catch (CustomersService.CustomersDoesNotExistException exn) {
            return halt(400, "The requested customer that you want to modify doesn't exist");
        }
    };

    /**
     * Removes a customer
     */
    static final Route deleteCustomer = (q, a) -> {
        final String id = q.params("id");
        LOG.info("Delete the user with id: '{}'", id);
        try {
            return CUSTOMERS_SERVICE.deleteCustomer(id);
        } catch (CustomersService.CustomersDoesNotExistException exn) {
            return halt(400, "The requested customer that you want to delete doesn't exist");
        }
    };

    /**
     * Checks the existence of a customer id
     */
    static final Route existCustomer = (q, a) -> {
        final String id = q.params("id");
        LOG.info("Checks the existence of a customer with id: '{}'", id);

        final boolean existCustomer = CUSTOMERS_SERVICE.existCustomer(id);
        return String.format("{ \"exists\": \"%s\" }", existCustomer ? "YES" : "NO");
    };

}
