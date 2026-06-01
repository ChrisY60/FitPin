package org.example.fitpinserver.business.serviceImplementations;

import org.example.fitpinserver.business.repositories.ProductRepository;
import org.example.fitpinserver.business.services.ProductService;
import org.example.fitpinserver.domain.models.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}
