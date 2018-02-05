package io.theam.repository;

import io.theam.model.Customer;
import io.theam.model.Purchase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@RunWith(SpringRunner.class)
@DataJpaTest
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
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
    public void repositorySavesPersonAndThenAddPurchase() {

        // Given: person saved
        Customer customer = new Customer();
        customer.setFirstName("Manuel");
        customer.setLastName("Mañoso");
        customer.setNdi("000000000X");
        customer.setPurchases(Collections.emptySet());
        customerRepository.save(customer);

        // When: load, add purchase and save
        Customer loadedCustomer = customerRepository.findByFirstName("Manuel");
        assertNotNull(loadedCustomer);
        assertEquals(0, loadedCustomer.getPurchases().size());
        Purchase purchase = new Purchase();
        purchase.setCustomer(loadedCustomer);
        purchase.setDate(new Date());
        purchase.setAmmount(100);
        Set<Purchase> newSet = new HashSet<>(loadedCustomer.getPurchases());
        newSet.add(purchase);
        loadedCustomer.setPurchases(newSet);
        customerRepository.save(loadedCustomer);

        // Then: reloaded there is one purchase
        Customer secondLoadedCustomer = customerRepository.findByFirstName("Manuel");
        assertNotNull(secondLoadedCustomer);
        assertEquals(1, secondLoadedCustomer.getPurchases().size());
    }
}
