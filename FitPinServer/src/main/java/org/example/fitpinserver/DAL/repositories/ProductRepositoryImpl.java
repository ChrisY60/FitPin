package org.example.fitpinserver.DAL.repositories;

import org.example.fitpinserver.DAL.mappers.ProductPersistenceMapper;
import org.example.fitpinserver.business.repositories.ProductRepository;
import org.example.fitpinserver.domain.models.Product;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJPARepository productJPARepository;
    private final ProductPersistenceMapper productPersistenceMapper;

    public ProductRepositoryImpl(ProductJPARepository productJPARepository, ProductPersistenceMapper productPersistenceMapper) {
        this.productJPARepository = productJPARepository;
        this.productPersistenceMapper = productPersistenceMapper;
    }

    @Override
    public List<Product> findAll() {
        return productJPARepository.findAll().stream()
                .map(productPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<Product> findAllByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return List.of();
        return productJPARepository.findAllById(ids).stream()
                .map(productPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<Product> searchByName(String query) {
        return productJPARepository.findByNameContainingIgnoreCase(query).stream()
                .map(productPersistenceMapper::toDomain)
                .toList();
    }
}
