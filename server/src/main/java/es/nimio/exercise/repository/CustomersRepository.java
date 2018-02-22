package es.nimio.exercise.repository;

import es.nimio.exercise.model.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface CustomersRepository extends CrudRepository<Customer, Long> {
	
	Collection<Customer> findAll();

	Collection<Customer> findByFirstName(String firstName);

	Collection<Customer> findByLastName(String lastName);

	Collection<Customer> findByNdi(String ndi);
}
