package io.theam.controller;

import io.theam.model.Purchase;
import io.theam.model.api.PurchaseData;
import io.theam.repository.PurchasesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/purchases")
public class PurchaseController {

    @Autowired
    private PurchasesRepository purchaseRepository;


    // has no sense allow a query of all purchases
    // Only allow ask for purchases of a customer or of a product

    private static Collection<PurchaseData> transform(final Collection<Purchase> purchases) {
        return  purchases.stream()
                .map(p -> new PurchaseData(
                        p.getDate(),
                        p.getCustomer().getId(),
                        p.getProduct().getId(),
                        p.getNumOfItems(),
                        p.getPriceOfItem()))
                .collect(Collectors.toList());
    }

    private static Collection<PurchaseData> readAndTransform(final Supplier<Collection<Purchase>> supplier) {
        return Optional.ofNullable(supplier.get())
                .map(PurchaseController::transform)
                .orElse(new ArrayList<>());
    }

    private static ResponseEntity<Collection<PurchaseData>> readAndAccpeted(final Supplier<Collection<Purchase>> supplier) {
        return new ResponseEntity<>(readAndTransform(supplier), HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/ofCustomer/{customerId}", method = RequestMethod.GET)
    public ResponseEntity<Collection<PurchaseData>> purchasesOfCustomer(@PathVariable(name="customerId")Long customerId) {
        return readAndAccpeted(() -> purchaseRepository.findByCustomerId(customerId));
    }

    @RequestMapping(value = "/ofProduct/{productId}", method = RequestMethod.GET)
    public ResponseEntity<Collection<PurchaseData>> purchasesOfProduct(@PathVariable(name="productId")Long productId) {
        return readAndAccpeted(() -> purchaseRepository.findByProductId(productId));
    }

    @RequestMapping(value = "/ofCustomer/{customerId}/ofProduct/{productId}", method = RequestMethod.GET)
    public ResponseEntity<Collection<PurchaseData>> purchasesOfCustomerAndProduct(
            @PathVariable(name="customerId")Long customerId,
            @PathVariable(name="customerId")Long productId) {
        return readAndAccpeted(() -> purchaseRepository.findByCustomerIdAndProductId(customerId, productId));
    }
}
