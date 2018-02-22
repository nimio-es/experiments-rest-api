package es.nimio.exercise.controller;

import es.nimio.exercise.model.Customer;
import es.nimio.exercise.model.Image;
import es.nimio.exercise.model.Purchase;
import es.nimio.exercise.model.api.*;
import es.nimio.exercise.repository.CustomersRepository;
import es.nimio.exercise.repository.ImagesRepository;
import es.nimio.exercise.repository.PurchasesRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Methods to use on the rest of controller to handle Customers
 */
public abstract class CustomerBaseController {

    @Autowired
    protected CustomersRepository customerRepo;

    @Autowired
    protected ImagesRepository imageRepository;

    @Autowired
    protected PurchasesRepository purchasesRepository;


    protected CustomerResponse toCustomerResponse(final Customer customer) {
        return toCustomerResponse(customer);
    }

    protected CustomerResponse toCustomerResponse(final Customer customer, final Image image) {
        return toCustomerResponse(customer, image, true, null);
    }

    protected CustomerResponse toCustomerResponse(
            final Customer customer,
            final Image image,
            final boolean onlyInfo,
            final Collection<Purchase> purchases) {

        final CustomerResponseImageData imageData = image != null
                ? (onlyInfo
                ? new CustomerResponseImageData.HasImage(image.getId())
                : new CustomerResponseImageData.Image(new ImageData(image.getFileName(), image.getFileData())))
                : CustomerResponseImageData.NoImage.INSTANCE;

        return
                new CustomerResponse(
                        customer.getId(),
                        new CustomerData(customer.getFirstName(), customer.getLastName(), customer.getNdi()),
                        imageData,
                        purchases != null && !purchases.isEmpty()
                                ? purchases.stream()
                                .map(p -> new PurchaseData(
                                        p.getDate(),
                                        p.getCustomer().getId(),
                                        p.getProduct().getId(),
                                        p.getNumOfItems(),
                                        p.getPriceOfItem()))
                                .collect(Collectors.toList())
                                : null
                );
    }

}
