package es.nimio.exercise.controller;

import es.nimio.exercise.model.Customer;
import es.nimio.exercise.model.api.CustomerResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Methods oriented to search customers
 */
@RestController
@RequestMapping("/customers/search")
public class CustomersSearchController extends CustomerBaseController {

    private ResponseEntity<Collection<CustomerResponse>> lookupUsing(final Supplier<Collection<Customer>> customerSearch) {
        return Optional
                .ofNullable(customerSearch.get())
                .map(cs-> new ResponseEntity<>(
                        (Collection<CustomerResponse>) // needed because the expected type isn't a collection...
                                cs.stream().map(c ->
                                        toCustomerResponse(c, imageRepository.findByCustomerId(c.getId())))
                                        .collect(Collectors.toList()),
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "firstName/{firstName}", method = RequestMethod.GET)
    public ResponseEntity<Collection<CustomerResponse>> lookupPersonFirstName(@PathVariable String firstName) {
        return lookupUsing(() -> customerRepo.findByFirstName(firstName));
    }

    @RequestMapping(value = "lastName/{lastName}", method = RequestMethod.GET)
    public ResponseEntity<Collection<CustomerResponse>> lookupPersonLastName(@PathVariable String lastName) {
        return lookupUsing(() -> customerRepo.findByLastName(lastName));
    }

    @RequestMapping(value = "ndi/{ndi}", method = RequestMethod.GET)
    public ResponseEntity<Collection<CustomerResponse>> lookupPersonNdi(@PathVariable String ndi) {
        return lookupUsing(() -> customerRepo.findByNdi(ndi));
    }
}
