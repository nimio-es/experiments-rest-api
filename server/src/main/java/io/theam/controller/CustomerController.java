package io.theam.controller;

import io.theam.model.Customer;
import io.theam.model.Image;
import io.theam.model.Purchase;
import io.theam.repository.CustomerRepository;
import io.theam.util.UtilBase64Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.security.Principal;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

	@Autowired
	private CustomerRepository customerRepo;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Collection<Customer>> getCustomerList() {
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
	public ResponseEntity<?> addCustomer(@RequestBody Customer customer) {
		return new ResponseEntity<>(customerRepo.save(customer), HttpStatus.CREATED);
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


    /*****
     * IMAGES
     *****/

    @RequestMapping(value = "/{id}/image", method = RequestMethod.POST)
    public ResponseEntity<Customer> addImageToCustomer(@PathVariable Long id, @RequestBody Image image) {

        logger.info("Associate an image to customer {}", id);
        logger.info(image.toString());

        // the file name
        final String image_id = UUID.randomUUID().toString();
        final String[] partsOfName = image.getName().split("\\.");  // TO NOT INCLUDE FILEUTILS ONLY FOR THIS CASE
        final String fileNameExtension =
                Optional.ofNullable(partsOfName[partsOfName.length - 1]).map(String::toLowerCase).orElse("");
        final File theamPath = new File(new File(System.getProperty("java.io.tmpdir")),"theam");
        if(!theamPath.exists()) theamPath.mkdir();
        final File imagesPath = new File(theamPath,"images");
        if(!imagesPath.exists()) imagesPath.mkdir(); // create the directory if it doesn't exist
        final String path =
                new File(imagesPath,image_id + "." + fileNameExtension).getAbsolutePath();

        if(!"jpg".equals(fileNameExtension) && !"gif".equals(fileNameExtension)) {
            throw new RuntimeException("Only allowed to work with JPG or GIF images");
        }

        // gets the customer
        final Customer customer = customerRepo.findOne(id);
        final String currentImage = customer.getImageId();
        customer.setImageId(image_id);

        // saves the image
        UtilBase64Image.decoder(image.getData(), path);

        // saves the modified customer
        final Customer savedCustomer = customerRepo.save(customer);

        // its necessary to remove the old image
        // TODO: to be done

        logger.info("All was saved!");

        return new ResponseEntity(savedCustomer, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/{id}/image", method = RequestMethod.GET)
    public Image getImageFromCustomer(@PathVariable Long id) {

        // gets the customer
        final Customer customer = customerRepo.findOne(id);
        final String currentImage = customer.getImageId();

        if(currentImage == null) {
            logger.info("The customer has no image associated");
            return null;
        }

        final File theamPath = new File(new File(System.getProperty("java.io.tmpdir")),"theam");
        if(!theamPath.exists()) theamPath.mkdir();
        final File imagesPath = new File(theamPath,"images");
        if(!imagesPath.exists()) imagesPath.mkdir(); // create the directory if it doesn't exist
        final File[] filesWithPrefix = imagesPath.listFiles(fn -> fn.getName().startsWith(currentImage));
        if(filesWithPrefix.length == 0) return null; // DOESN'T EXIST
        final String imagePath = filesWithPrefix[0].getAbsolutePath();
        final String imageBase64 = UtilBase64Image.encoder(imagePath);

        if(imageBase64 != null) {
            Image image = new Image(new File(imagePath).getName(), imageBase64);
            return image;
        }
        return null;
    }
}
