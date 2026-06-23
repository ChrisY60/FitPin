package org.example.fitpinserver.DAL.repositories;

import org.example.fitpinserver.DAL.elasticsearch.ProductDocument;
import org.example.fitpinserver.DAL.repositories.elasticsearch.ProductSearchRepository;
import org.example.fitpinserver.DAL.entities.BrandEntity;
import org.example.fitpinserver.DAL.entities.ProductEntity;
import org.example.fitpinserver.DAL.mappers.ProductPersistenceMapper;
import org.example.fitpinserver.business.repositories.ProductRepository;
import org.example.fitpinserver.business.services.SearchIndexService;
import org.example.fitpinserver.domain.models.Brand;
import org.example.fitpinserver.domain.models.Product;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJPARepository productJPARepository;
    private final ProductPersistenceMapper productPersistenceMapper;
    private final ProductSearchRepository productSearchRepository;
    private final BrandJPARepository brandJPARepository;
    private final SearchIndexService searchIndexService;

    public ProductRepositoryImpl(ProductJPARepository productJPARepository, ProductPersistenceMapper productPersistenceMapper,
                                  ProductSearchRepository productSearchRepository, BrandJPARepository brandJPARepository,
                                  SearchIndexService searchIndexService) {
        this.productJPARepository = productJPARepository;
        this.productPersistenceMapper = productPersistenceMapper;
        this.productSearchRepository = productSearchRepository;
        this.brandJPARepository = brandJPARepository;
        this.searchIndexService = searchIndexService;
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

    @Override
    public Optional<Product> findByNameIgnoreCaseAndBrandId(String name, Long brandId) {
        return productJPARepository.findByNameIgnoreCaseAndBrand_Id(name, brandId)
                .map(productPersistenceMapper::toDomain);
    }

    @Override
    public Product save(Product product) {
        BrandEntity brandEntity = brandJPARepository.findById(product.getBrand().getId())
                .orElseThrow(() -> new RuntimeException("Brand not found: " + product.getBrand().getId()));
        ProductEntity entity = new ProductEntity(product.getName(), brandEntity);
        entity.setImageUrl(product.getImageUrl());
        ProductEntity saved = productJPARepository.save(entity);
        Product savedDomain = productPersistenceMapper.toDomain(saved);
        searchIndexService.indexProduct(savedDomain);
        return savedDomain;
    }

    private static Product toDomain(ProductDocument document) {
        Brand brand = document.getBrandId() == null ? null : new Brand(document.getBrandId(), document.getBrandName());
        Product product = new Product(document.getId(), document.getName(), brand);
        product.setImageUrl(document.getImageUrl());
        return product;
    }
}
