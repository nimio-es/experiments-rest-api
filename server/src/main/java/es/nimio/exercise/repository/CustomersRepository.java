package es.nimio.exercise.repository;

import es.nimio.exercise.model.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface CustomersRepository extends CrudRepository<Customer, Long> {
	
	List<Customer> findAll();

	List<Customer> findByFirstName(String firstName);

	List<Customer> findByLastName(String lastName);

	List<Customer> findByNdi(String ndi);
}
