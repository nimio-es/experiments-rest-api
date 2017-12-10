package io.theam.model;

import com.google.gson.Gson;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Essential data of a Customer (without purchase info)
 */
public @Data class Customer {

    private static final Logger LOG = LoggerFactory.getLogger(Customer.class);

    private final String id;
    private final String firstName;
    private final String lastName;
    private final String nid;
    private final String imageStoreReference;

    public Customer(
            final String id,
            final String firstName,
            final String lastName,
            final String nid) throws InvalidCustomerDataException {
        this(id, firstName, lastName, nid, null);
    }

    public Customer(
            final String id,
            final String firstName,
            final String lastName,
            final String nid,
            final String imageStoreReference) throws InvalidCustomerDataException {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nid = nid;
        this.imageStoreReference = imageStoreReference;
        validateCustomerData(this, true);
    }

    public static Customer fromJson(final String body) throws InvalidCustomerDataException {
        try {
            return new Gson().fromJson(body, Customer.class);
        } catch (Exception ex) {
            if (ex instanceof InvalidCustomerDataException)
                throw (InvalidCustomerDataException) ex;
            throw ex;
        }
    }

    public static boolean validateCustomerData(final Customer customer) throws InvalidCustomerDataException {
        return validateCustomerData(customer, false);
    }

    public static boolean validateCustomerData(final Customer customer, final boolean launchException) throws InvalidCustomerDataException {

        final boolean correct =
                customer.id != null
                        && !"".equals(customer.id.trim())
                        && customer.firstName != null
                        && !"".equals(customer.firstName.trim())
                        && customer.lastName != null
                        && !"".equals(customer.lastName.trim())
                        && customer.nid != null;

        if (!correct) {
            if (!launchException) return false;
            else throw new InvalidCustomerDataException();
        }
        return true;
    }

    public String getId() {
        return id;
    }

    /**
     * Exception class to provide information when the customer id doesn't exist
     */
    public static final class InvalidCustomerDataException extends Exception {

        public InvalidCustomerDataException() {
            super("The Customer data isn't correct!");
        }
    }
}

