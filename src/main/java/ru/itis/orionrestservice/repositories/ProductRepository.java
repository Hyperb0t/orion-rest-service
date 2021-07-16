package ru.itis.orionrestservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.itis.orionrestservice.models.Product;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "select product_maxtime.id, product_maxtime.name, product_maxtime.price\n" +
            "from (select product.id, product.name, product.price, max(ale.timestamp) as maxtime\n" +
            "      from product\n" +
            "               join access_log_entry ale\n" +
            "                    on product.id = ale.product_id\n" +
            "      group by product.id) as product_maxtime\n" +
            "where now() - maxtime > cast(:interval as interval) ", nativeQuery = true)
    List<Product> getProductsToDelete(@Param("interval")String interval);
}
