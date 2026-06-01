package org.example.fitpinserver.DAL.mappers;

import org.example.fitpinserver.DAL.entities.ProductEntity;
import org.example.fitpinserver.domain.models.Brand;
import org.example.fitpinserver.domain.models.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductPersistenceMapper {

    public Product toDomain(ProductEntity entity) {
        Brand brand = new Brand(entity.getBrand().getId(), entity.getBrand().getName());
        Product product = new Product(entity.getId(), entity.getName(), brand);
        product.setImageUrl(entity.getImageUrl());
        return product;
    }
}
