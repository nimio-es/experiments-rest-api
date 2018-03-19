package es.nimio.exercise.repository;

import es.nimio.exercise.model.Customer;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@DataJpaTest
public class CustomerRepositoryTest {

    @Autowired
    private CustomersRepository customerRepository;

    @Test
    @Ignore
    public void repositorySavesPerson() {
        Customer customer = new Customer();
        customer.setFirstName("Oscar");
        customer.setLastName("Pepino");
        customer.setNdi("000000000X");
        
        Customer result = customerRepository.save(customer);
        
        assertEquals(result.getFirstName(), "Oscar");
        assertEquals(result.getLastName(), "Pepino");
        assertEquals(result.getNdi(), "000000000X");
    }

    @Test
    @Ignore
    public void repositorySavesPersonAndFindByFirstName() {

        // Given: person saved
        Customer customer = new Customer();
        customer.setFirstName("Manuel");
        customer.setLastName("Mañoso");
        customer.setNdi("000000000X");
        assertNotNull(customerRepository.save(customer));

        // Then: located using first name
        Collection<Customer> loadedCustomers = customerRepository.findByFirstName("Manuel");
        assertNotNull(loadedCustomers);
        assertFalse(loadedCustomers.isEmpty());
        Customer loadedCustomer = new ArrayList<>(loadedCustomers).get(0);
        assertNotNull(loadedCustomer);
    }
}
