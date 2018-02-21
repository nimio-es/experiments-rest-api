package es.nimio.exercise.repository;

import es.nimio.exercise.model.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface CustomersRepository extends CrudRepository<Customer, Long> {
	
	Collection<Customer> findAll();
	
	Customer findByFirstName(String firstName);

	Customer findByLastName(String lastName);

	Customer findByNdi(String ndi);
}
