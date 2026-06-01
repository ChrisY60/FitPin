package org.example.fitpinserver.business.repositories;

import org.example.fitpinserver.domain.models.Product;

import java.util.List;

public interface ProductRepository {
    List<Product> findAll();
    List<Product> findAllByIds(List<Long> ids);
}
