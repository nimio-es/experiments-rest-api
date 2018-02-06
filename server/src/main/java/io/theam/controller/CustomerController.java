package io.theam.controller;

import io.theam.model.Customer;
import io.theam.model.Image;
import io.theam.model.api.ImageData;
import io.theam.repository.CustomerRepository;
import io.theam.repository.ImageRepository;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
	private CustomerRepository customerRepo;

    @Autowired
    private ImageRepository imageRepository;

    /*****
     * CUSTOMERS
     *****/

    @RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Collection<Customer>> getCustomerList() {
		return new ResponseEntity<>(customerRepo.findAll(), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Customer> getPerson(@PathVariable long id) {
        return Optional
                .ofNullable(customerRepo.findOne(id))
                .map(c -> new ResponseEntity<>(c, HttpStatus.CREATED))
                .orElse(new ResponseEntity<>((Customer)null, HttpStatus.NOT_FOUND));
	}

	@RequestMapping(value = "firstName/{firstName}", method = RequestMethod.GET)
	public ResponseEntity<Customer> lookupPersonFirstName(@PathVariable String firstName) {
        return Optional
                .ofNullable(customerRepo.findByFirstName(firstName))
                .map(c -> new ResponseEntity<>(c, HttpStatus.CREATED))
                .orElse(new ResponseEntity<>((Customer)null, HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "lastName/{lastName}", method = RequestMethod.GET)
    public ResponseEntity<Customer> lookupPersonLastName(@PathVariable String lastName) {
        return Optional
                .ofNullable(customerRepo.findByLastName(lastName))
                .map(c -> new ResponseEntity<>(c, HttpStatus.CREATED))
                .orElse(new ResponseEntity<>((Customer)null, HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "ndi/{ndi}", method = RequestMethod.GET)
    public ResponseEntity<Customer> lookupPersonNdi(@PathVariable String ndi) {
        return Optional
                .ofNullable(customerRepo.findByNdi(ndi))
                .map(c -> new ResponseEntity<>(c, HttpStatus.CREATED))
                .orElse(new ResponseEntity<>((Customer)null, HttpStatus.NOT_FOUND));
    }

    @RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addCustomer(@RequestBody Customer customer) {
		return new ResponseEntity<>(customerRepo.save(customer), HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteCustomer(@PathVariable long id, Principal principal) {
		customerRepo.delete(id);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

/*
	@RequestMapping(value = "/{id}/purchases", method = RequestMethod.GET)
	public ResponseEntity<Collection<Purchase>> getPersonParties(@PathVariable long id) {
		Customer person = customerRepo.findOne(id);

		if (person != null) {
			return new ResponseEntity<>(person.getPurchases(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>((Collection<Purchase>)null, HttpStatus.NOT_FOUND);
		}
	}
*/

    /*****
     * IMAGES
     *****/

    @RequestMapping(value = "/{id}/image", method = RequestMethod.POST)
    public ResponseEntity<ImageData> addImageToCustomer(@PathVariable final Long id, @RequestBody final ImageData image) {

        logger.info("Associate an image to customer {}", id);

        // search the image
        Image storedImage = imageRepository.findByCustomerId(id);
        if(storedImage == null) {
            storedImage = new Image();
            storedImage.setCustomer(customerRepo.findOne(id));
        }
        storedImage.setFileName(image.getFileName());
        storedImage.setFileData(image.getFileData());

        Image savedImage = imageRepository.save(storedImage);
        return new ResponseEntity(
                savedImage != null ? image : null,
                savedImage != null ? HttpStatus.ACCEPTED : HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/{id}/image", method = RequestMethod.GET)
    public ResponseEntity<ImageData> getImageFromCustomer(@PathVariable Long id) throws IOException {

        // search the image
        final Image image =  imageRepository.findByCustomerId(id);

        logger.info("{}", new ObjectMapper().writeValueAsString(image));

        if(image == null) {
            return new ResponseEntity((ImageData)null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(new ImageData(image.getFileName(), image.getFileData()), HttpStatus.OK);
    }
}
