package ru.itis.orionrestservice.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.orionrestservice.models.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
