package org.example.fitpinserver.business.repositories;

import org.example.fitpinserver.domain.models.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    List<Product> findAll();
    List<Product> findAllByIds(List<Long> ids);
    List<Product> searchByName(String query);
    Optional<Product> findByNameIgnoreCaseAndBrandId(String name, Long brandId);
    Product save(Product product);
}
