package org.example.fitpinserver.DAL.repositories;

import org.example.fitpinserver.DAL.elasticsearch.ProductDocument;
import org.example.fitpinserver.DAL.elasticsearch.ProductSearchRepository;
import org.example.fitpinserver.DAL.mappers.ProductPersistenceMapper;
import org.example.fitpinserver.business.repositories.ProductRepository;
import org.example.fitpinserver.domain.models.Brand;
import org.example.fitpinserver.domain.models.Product;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJPARepository productJPARepository;
    private final ProductPersistenceMapper productPersistenceMapper;
    private final ProductSearchRepository productSearchRepository;

    public ProductRepositoryImpl(ProductJPARepository productJPARepository, ProductPersistenceMapper productPersistenceMapper,
                                  ProductSearchRepository productSearchRepository) {
        this.productJPARepository = productJPARepository;
        this.productPersistenceMapper = productPersistenceMapper;
        this.productSearchRepository = productSearchRepository;
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
        return productSearchRepository.searchByName(query).stream()
                .map(ProductRepositoryImpl::toDomain)
                .toList();
    }

    private static Product toDomain(ProductDocument document) {
        Brand brand = document.getBrandId() == null ? null : new Brand(document.getBrandId(), document.getBrandName());
        Product product = new Product(document.getId(), document.getName(), brand);
        product.setImageUrl(document.getImageUrl());
        return product;
    }
}
