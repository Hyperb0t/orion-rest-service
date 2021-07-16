package ru.itis.orionrestservice.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.itis.orionrestservice.repositories.ProductRepository;

@Service
@Slf4j
public class LogCleanService {

    private ProductRepository productRepository;

    public LogCleanService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Scheduled(fixedRate = 60000)
    private void deleteExpiredProducts() {
        var products = productRepository.getProductsToDelete("00:01");
        log.info("Deleting " + products.size() +" unused products");
        products.forEach(productRepository::delete);
    }
}
