package io.theam.controller;

import io.theam.model.Customer;
import io.theam.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepo;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Collection<Customer>> getCustomers() {
        return new ResponseEntity<>(customerRepo.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Customer> getCustomer(@PathVariable long id) {
        return Optional.ofNullable(customerRepo.findOne(id))
                .map(c -> new ResponseEntity<>(c, HttpStatus.OK))
                .orElse(new ResponseEntity<>((Customer)null, HttpStatus.NOT_FOUND));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> addCustomer(@RequestBody Customer customer) {
        return new ResponseEntity<>(customerRepo.save(customer), HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<?> updatePerson(@RequestBody Customer customer) {
        return updatePerson(customer.getId(), customer);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updatePerson(@PathVariable long id, @RequestBody Customer customer) {
        if(id == customer.getId())
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);

        Customer oldCustomer = customerRepo.findOne(id);
        if(oldCustomer == null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        // modify
        oldCustomer.setFirstName(customer.getFirstName());
        oldCustomer.setFamilyName(customer.getFamilyName());
        oldCustomer.setNdi(customer.getNdi());
        oldCustomer.setImageUri(customer.getImageUri());
        customerRepo.save(oldCustomer);

        return new ResponseEntity<>(oldCustomer, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteCustomer(@PathVariable long id) {
        customerRepo.delete(id);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
