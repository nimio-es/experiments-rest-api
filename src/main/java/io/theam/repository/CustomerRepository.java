package io.theam.repository;

import io.theam.model.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {
	
	Collection<Customer> findAll();
	
	Customer findByFirstName(String username);

	Customer findByLastName(String username);
}
