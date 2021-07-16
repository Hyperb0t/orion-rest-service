package ru.itis.orionrestservice.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
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

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("/products")
    public Product newProduct(@RequestBody Product newProduct, @AuthenticationPrincipal UserDetails userDetails) {
        log.info("adding product: " + newProduct);
        log.info("by user (full info): " + userDetails.toString());
        log.info("user has following roles:");
        userDetails.getAuthorities().stream().map(Object::toString).forEach(log::info);
        return repository.save(newProduct);
    }


    @GetMapping("/products/{id}")
    public Product one(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Product with id " + id + " not found"));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
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
                    log.info("product with id " + id + " not found to update, creating new");
                    newProduct.setId(id);
                    return repository.save(newProduct);
                });
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/products/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        try {
            log.info("deleting product with id " + id);
            repository.deleteById(id);
        }
        catch (IllegalArgumentException | EmptyResultDataAccessException e) {
            log.info("can't delete non-existent product with id " + id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Product with id " + id + " not found", e);
        }
    }
}

