package es.nimio.exercise.controller;

import es.nimio.exercise.model.Customer;
import es.nimio.exercise.model.Image;
import es.nimio.exercise.model.api.CustomerData;
import es.nimio.exercise.model.api.CustomerResponse;
import es.nimio.exercise.model.api.CustomerResponseImageData;
import es.nimio.exercise.model.api.ImageData;
import es.nimio.exercise.repository.CustomersRepository;
import es.nimio.exercise.repository.ImagesRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Methods to use on the rest of controller to handle Customers
 */
public abstract class CustomerBaseController {

    @Autowired
    protected CustomersRepository customerRepo;

    @Autowired
    protected ImagesRepository imageRepository;


    protected CustomerResponse toCustomerResponse(final Customer customer) {
        return toCustomerResponse(customer);
    }

    protected CustomerResponse toCustomerResponse(final Customer customer, final Image image) {
        return toCustomerResponse(customer, image, true);
    }

    protected CustomerResponse toCustomerResponse(final Customer customer, final Image image, final boolean onlyInfo) {

        final CustomerResponseImageData imageData = image != null
                ? (onlyInfo
                ? new CustomerResponseImageData.HasImage(image.getId())
                : new CustomerResponseImageData.Image(new ImageData(image.getFileName(), image.getFileData())))
                : CustomerResponseImageData.NoImage.INSTANCE;

        return
                new CustomerResponse(
                        customer.getId(),
                        new CustomerData(customer.getFirstName(), customer.getLastName(), customer.getNdi()),
                        imageData
                );
    }

}
