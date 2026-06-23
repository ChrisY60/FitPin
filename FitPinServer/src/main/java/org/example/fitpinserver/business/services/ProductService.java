package org.example.fitpinserver.business.services;

import org.example.fitpinserver.domain.models.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();
    Product createProduct(String name, Long brandId, String imageUrl);
}
