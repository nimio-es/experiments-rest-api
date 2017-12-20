package io.theam.repository;

import io.theam.model.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

    Collection<Customer> findAll();

    Customer findByFirstName(String firstName);
}
