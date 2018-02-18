package io.theam.repository;

import io.theam.model.Customer;
import io.theam.model.Product;
import io.theam.model.Purchase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PurchasesRepositoryTest {

    @Autowired
    private CustomersRepository customersRepository;

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private PurchasesRepository purchasesRepository;

    @Test
    public void repositorySavesPurchase() {

        final Customer savedCustomer = customersRepository.save(
                new Customer("Pepe", "Potamo", "987654"));

        final Product savedProduct = productsRepository.save(
                new Product("JUGUETO", "PLAYMOBIL",10.01));

        Purchase purchase = new Purchase();
        purchase.setCustomer(savedCustomer);
        purchase.setProduct(savedProduct);
        purchase.setDate(new Date());
        purchase.setNumOfItems(20);
        purchase.setPriceOfItem(8.92);

        final Purchase result = purchasesRepository.save(purchase);


        assertEquals(result.getNumOfItems(), 20);
        assertEquals(result.getCustomer().getFirstName(), savedCustomer.getFirstName());
        assertEquals(result.getProduct().getName(), savedProduct.getName());
    }


}
