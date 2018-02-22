package es.nimio.exercise.controller;

import es.nimio.exercise.model.Customer;
import es.nimio.exercise.model.Image;
import es.nimio.exercise.model.api.CustomerData;
import es.nimio.exercise.model.api.CustomerResponse;
import es.nimio.exercise.model.api.ImageData;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

/**
 * Methods oriented to the entity and not for the collection
 */
@RestController
@RequestMapping("/customers/{id}")
public class CustomerController extends CustomerBaseController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<CustomerResponse> getCustomer(
            @PathVariable long id,
            @RequestParam(name = "includeImage", required = false, defaultValue = "false") boolean includeImage,
            @RequestParam(name = "includePurchases", required = false, defaultValue = "false") boolean includePurchases) {

        // the image includes the customer
        final Image image = imageRepository.findByCustomerId(id);
        if(image != null)
            return new ResponseEntity<>(
                    toCustomerResponse(image.getCustomer(), image, !includeImage,
                            includePurchases ? purchasesRepository.findByCustomerId(id) : null),
                    HttpStatus.OK);

        return Optional
                .ofNullable(customerRepo.findOne(id))
                .map(c -> new ResponseEntity<>(toCustomerResponse(c, null), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> postTheCustomer(@PathVariable long id) {
        return customerRepo.exists(id)
                ? new ResponseEntity<>(HttpStatus.CONFLICT)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<CustomerResponse> replaceCustomer(@PathVariable long id, @RequestBody CustomerData customer) {
        final Customer loadedCustomer = customerRepo.findOne(id);
        if(loadedCustomer == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if(StringUtils.isEmpty(customer.getFirstName())
            || StringUtils.isEmpty(customer.getLastName())
            || StringUtils.isEmpty(customer.getNdi()))
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        loadedCustomer.setFirstName(customer.getFirstName());
        loadedCustomer.setLastName(customer.getLastName());
        loadedCustomer.setNdi(customer.getNdi());
        return Optional
                .ofNullable(customerRepo.save(loadedCustomer))
                .map(c -> new ResponseEntity<>(toCustomerResponse(c), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.INSUFFICIENT_STORAGE));
    }

    /**
     * Deletes one customer, if it exists
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteCustomer(@PathVariable long id) {
        if(customerRepo.exists(id)) {
            customerRepo.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /*****
     * IMAGES
     *****/

    @RequestMapping(value = "image", method = RequestMethod.POST)
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
        return new ResponseEntity<>(
                savedImage != null ? image : null,
                savedImage != null ? HttpStatus.ACCEPTED : HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "image", method = RequestMethod.GET)
    public ResponseEntity<ImageData> getImageFromCustomer(@PathVariable Long id) throws IOException {

        // search the image
        final Image image =  imageRepository.findByCustomerId(id);

        logger.info("{}", new ObjectMapper().writeValueAsString(image));

        if(image == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(
                new ImageData(
                        image.getFileName(),
                        image.getFileData()),
                HttpStatus.OK);
    }

}
