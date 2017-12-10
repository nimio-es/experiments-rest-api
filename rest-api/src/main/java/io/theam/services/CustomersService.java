package io.theam.services;

import io.theam.model.Customer;
import lombok.Data;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * A parody of an DAO Service for Customers.
 */
public final class CustomersService {

    private static Map<String, Customer> customers = Collections.synchronizedMap(new TreeMap<>());
    private static AtomicInteger customerCounter = new AtomicInteger(0);

    // ---
    // initial values
    // ---
    static {

        try {
            customers.put(
                    "1",
                    new Customer("1", "Saulo", "Alvarado Mateos", "000000000A"));

            customers.put(
                    "2",
                    new Customer("2", "Jaime", "López", "11111111B"));

            customers.put(
                    "3",
                    new Customer("3", "Nayara", "Rodriguez Rodríguez", "222222222C"));

            // the current counter
            customerCounter.set(3);

        } catch (Customer.InvalidCustomerDataException e) {
            e.printStackTrace();
        }

    }

    // -----
    // Operations
    // -----

    public List<Customer> allCustomers(final boolean sorted) {
        if(!sorted)
            return new ArrayList<>(customers.values());
        else
            return
                    customers.values()
                            .stream()
                            .sorted(Comparator.comparing(Customer::getId))
                            .collect(Collectors.toList());
    }

    public Customer getCustomer(final String id) throws CustomersDoesNotExistException {
        return Optional.ofNullable(customers.get(id))
                .orElseThrow(() -> new CustomersDoesNotExistException(id));
    }

    public Customer addCustomer(final PartialCreationCustomerData partialCustomer) throws Customer.InvalidCustomerDataException {
        final String newId = Integer.toString(customerCounter.addAndGet(1));
        final Customer newCustomer = partialCustomer.toCustomerWithId(newId);
        customers.put(newId, newCustomer);
        return newCustomer;
    }

    public Customer editCustomer(final Customer editCustomer) throws CustomersDoesNotExistException {
        // checks the existence of the customer Id
        if(!customers.containsKey(editCustomer.getId()))
            throw new CustomersDoesNotExistException(editCustomer.getId());

        customers.put(editCustomer.getId(), editCustomer);
        return editCustomer;
    }

    public DeleteCustomerResponse deleteCustomer(final String id) throws CustomersDoesNotExistException {
        if(!customers.containsKey(id))
            throw new CustomersDoesNotExistException(id);

        final Customer deletedCustomer = customers.remove(id);
        return new DeleteCustomerResponse(deletedCustomer);
    }

    public boolean existCustomer(final String id) {
        return customers.containsKey(id);
    }

    /**
     * Utility class that represent the response of delete customer operation
     */
    public static @Data class DeleteCustomerResponse {

        private static final String RESULT_MESSAGE = "Customer with id '%s' was deleted";

        private final String deleteResult;
        private final Customer customer;

        public DeleteCustomerResponse(final Customer customer) {
            this.deleteResult = String.format(RESULT_MESSAGE, customer.getId());
            this.customer = customer;
        }
    }

    /**
     * Partial customer data for creating process
     */
    public static class PartialCreationCustomerData {

        private final String firstName;
        private final String lastName;
        private final String nid;

        public PartialCreationCustomerData(final String firstName, final String lastName, final String nid) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.nid = nid;
        }

        public Customer toCustomerWithId(final String id) throws Customer.InvalidCustomerDataException {
            return new Customer(id, firstName, lastName, nid);
        }
    }

    // -----
    // Exceptions
    // -----

    public static final class CustomersDoesNotExistException extends Exception {

        public CustomersDoesNotExistException(final String id) {
            super(String.format("There isn't a customer with id '%s'", id));
        }
    }

}
