package io.theam.controller;

import io.theam.model.Customer;
import io.theam.model.Purchase;
import io.theam.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;

@RestController
@RequestMapping("/customers")
public class CustomerController {

	@Autowired
	private CustomerRepository customerRepo;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Collection<Customer>> getPeople() {
		return new ResponseEntity<>(customerRepo.findAll(), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Customer> getPerson(@PathVariable long id) {
		Customer person = customerRepo.findOne(id);

		if (person != null) {
			return new ResponseEntity<>(customerRepo.findOne(id), HttpStatus.OK);
		} else {
			return new ResponseEntity<>((Customer)null, HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addCustomer(@RequestBody Customer person) {
		return new ResponseEntity<>(customerRepo.save(person), HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteCustomer(@PathVariable long id, Principal principal) {
		customerRepo.delete(id);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/purchases", method = RequestMethod.GET)
	public ResponseEntity<Collection<Purchase>> getPersonParties(@PathVariable long id) {
		Customer person = customerRepo.findOne(id);

		if (person != null) {
			return new ResponseEntity<>(person.getPurchases(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>((Collection<Purchase>)null, HttpStatus.NOT_FOUND);
		}
	}

}
