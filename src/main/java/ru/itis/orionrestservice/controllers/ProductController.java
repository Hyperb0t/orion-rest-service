package ru.itis.orionrestservice.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.itis.orionrestservice.models.Product;
import ru.itis.orionrestservice.repositories.ProductRepository;

import java.util.List;

@RestController
@Slf4j
public class ProductController {

    private final ProductRepository repository;

    public ProductController(ProductRepository repository) {
        this.repository = repository;
    }


    @GetMapping("/products")
    public List<Product> all() {
        log.info("returning all products");
        return repository.findAll();
    }

    @PostMapping("/products")
    public Product newProduct(@RequestBody Product newProduct) {
        return repository.save(newProduct);
    }


    @GetMapping("/products/{id}")
    public Product one(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Product with id " + id + " not found"));
    }

    @PutMapping("/products/{id}")
    public Product replaceProduct(@RequestBody Product newProduct, @PathVariable Long id) {

        return repository.findById(id)
                .map(employee -> {
                    log.info("updating product with id " + id);
                    employee.setName(newProduct.getName());
                    employee.setPrice(newProduct.getPrice());
                    return repository.save(employee);
                })
                .orElseGet(() -> {
                    log.info("product with id " + id + "not found to update, creating new");
                    newProduct.setId(id);
                    return repository.save(newProduct);
                });
    }

    @DeleteMapping("/products/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        repository.deleteById(id);
    }
}

