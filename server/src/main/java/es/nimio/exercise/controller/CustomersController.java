package es.nimio.exercise.controller;

import es.nimio.exercise.model.Customer;
import es.nimio.exercise.model.Image;
import es.nimio.exercise.model.api.CustomerData;
import es.nimio.exercise.model.api.CustomerResponse;
import es.nimio.exercise.model.api.CustomerResponseImageData;
import es.nimio.exercise.model.api.ImageData;
import es.nimio.exercise.repository.ImagesRepository;
import es.nimio.exercise.repository.CustomersRepository;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customers")
public class CustomersController extends CustomerBaseController {

    @RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Collection<CustomerResponse>> getCustomerList() {
		return new ResponseEntity<>(
		        customerRepo.findAll().stream()
                        .map(c -> toCustomerResponse(c, imageRepository.findByCustomerId(c.getId())))
                        .collect(Collectors.toList()),
                HttpStatus.OK);
	}

    /**
     * Store the information of a new customer
     * @param customer
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addCustomer(@RequestBody CustomerData customer) {

        final Customer customerToSave = new Customer();
        customerToSave.setFirstName(customer.getFirstName());
        customerToSave.setLastName(customer.getLastName());
        customerToSave.setNdi(customer.getNdi());

        final Customer saved = customerRepo.save(customerToSave);

		return new ResponseEntity<>(
		        toCustomerResponse(saved, null),
                saved != null ? HttpStatus.CREATED: HttpStatus.NOT_FOUND);
	}

    /*
     * Methods not allowed on collections
     */

    @RequestMapping(method = { RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE })
	public ResponseEntity<?> notAllowedOnEntireList() {
        return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
    }

}
