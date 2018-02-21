package es.nimio.exercise.controller;

import es.nimio.exercise.model.api.PurchaseData;
import es.nimio.exercise.repository.ProductsRepository;
import es.nimio.exercise.model.Purchase;
import es.nimio.exercise.repository.CustomersRepository;
import es.nimio.exercise.repository.PurchasesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/purchases")
public class PurchasesController {

    @Autowired
    private PurchasesRepository purchaseRepository;

    @Autowired
    private CustomersRepository customersRepository;

    @Autowired
    private ProductsRepository productsRepository;


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
                .map(PurchasesController::transform)
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

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<PurchaseData> registerPurchase(@RequestBody @Valid PurchaseData purchaseData) {
        final Purchase purchase = new Purchase();
        purchase.setDate(purchaseData.getDate());
        purchase.setCustomer(customersRepository.findOne(purchaseData.getCustomerId()));
        purchase.setProduct(productsRepository.findOne(purchaseData.getProductId()));
        purchase.setNumOfItems(purchaseData.getNumItems());
        purchase.setPriceOfItem(purchaseData.getPriceOfItem());

        return Optional.ofNullable(purchaseRepository.save(purchase))
                .map(p -> new ResponseEntity<>(
                        new PurchaseData(
                                p.getDate(),
                                p.getCustomer().getId(),
                                p.getProduct().getId(),
                                p.getNumOfItems(),
                                p.getPriceOfItem()),
                        HttpStatus.CREATED))
                .orElse(new ResponseEntity<>((PurchaseData)null, HttpStatus.BAD_REQUEST));
    }

}
