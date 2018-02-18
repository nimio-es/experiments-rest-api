package io.theam.controller;

import io.theam.model.Product;
import io.theam.model.api.ProductData;
import io.theam.model.api.ProductResponse;
import io.theam.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @RequestMapping
    public ResponseEntity<Collection<ProductResponse>> getAllProducts() {
        final Iterable<Product> loadedProducts = productRepository.findAll();
        return new ResponseEntity<>(
                loadedProducts != null
                        ? StreamSupport.stream(loadedProducts.spliterator(), true)
                        .map(p -> new ProductResponse(p.getId(),
                                new ProductData(p.getRef(), p.getName(), p.getCommonPrice())))
                        .collect(Collectors.toList())
                        :new ArrayList<ProductResponse>(),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<ProductResponse> getProduct(@PathVariable(name="id")Long id) {
        return
                Optional.ofNullable(productRepository.findOne(id))
                    .map(p -> new ResponseEntity<>(
                            new ProductResponse(
                                    p.getId(),
                                    new ProductData(p.getRef(), p.getName(), p.getCommonPrice())),
                            HttpStatus.OK))
                .orElse(new ResponseEntity<>((ProductResponse)null, HttpStatus.NOT_FOUND));
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
