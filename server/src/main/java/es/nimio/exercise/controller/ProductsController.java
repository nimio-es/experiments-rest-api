package es.nimio.exercise.controller;

import es.nimio.exercise.model.Product;
import es.nimio.exercise.model.api.ProductData;
import es.nimio.exercise.model.api.ProductResponse;
import es.nimio.exercise.repository.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductsRepository productRepository;

    @RequestMapping
    public ResponseEntity<Collection<ProductResponse>> getAllProducts() {
        final Iterable<Product> loadedProducts = productRepository.findAll();
        return new ResponseEntity<>(
                StreamSupport.stream(loadedProducts.spliterator(), true)
                        .map(p -> new ProductResponse(p.getId(),
                                new ProductData(p.getRef(), p.getName(), p.getCommonPrice())))
                        .collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<ProductResponse> getProduct(@PathVariable(name = "id") Long id) {
        return
                productRepository.findById(id)
                        .map(p -> new ResponseEntity<>(
                                new ProductResponse(
                                        p.getId(),
                                        new ProductData(p.getRef(), p.getName(), p.getCommonPrice())),
                                HttpStatus.OK))
                        .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse insertProduct(@RequestBody ProductData productData) {
        final Product product = new Product();
        product.setRef(productData.getRef());
        product.setName(productData.getName());
        product.setCommonPrice(productData.getCommonPrice());

        final Product savedProduct = productRepository.save(product);
        return
                new ProductResponse(
                        savedProduct.getId(),
                        new ProductData(
                                savedProduct.getRef(),
                                savedProduct.getName(),
                                savedProduct.getCommonPrice()));

    }
}
