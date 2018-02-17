package io.theam.controller;

import io.theam.model.Product;
import io.theam.model.api.ProductData;
import io.theam.model.api.ProductResponse;
import io.theam.repository.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductsRepository productRepository;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<ProductData> getAllProducts() {

        final Collection<ProductData> products = new LinkedList<>();

        for (final Product product : productRepository.findAll()) {
            products.add(new ProductData(product.getRef(), product.getName(), product.getCommonPrice()));
        }

        return products;
    }

    @GetMapping(params = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse getProduct(@PathVariable(name="id")Long id) {
        return Optional.ofNullable(productRepository.findOne(id))
                .map(p -> new ProductResponse(
                        p.getId(),
                        new ProductData(p.getRef(), p.getName(), p.getCommonPrice())))
                .orElse(null);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse insertProduct(@RequestBody ProductData productData) {
        final Product product = new Product();
        product.setRef(productData.getRef());
        product.setName(productData.getName());
        product.setCommonPrice(productData.getCommonPrice());

        final Product savedProduct = productRepository.save(product);
        if(savedProduct!=null)
            return
                    new ProductResponse(
                            savedProduct.getId(),
                            new ProductData(
                                    savedProduct.getRef(),
                                    savedProduct.getName(),
                                    savedProduct.getCommonPrice()));

        return null;
    }
}
