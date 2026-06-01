package org.example.fitpinserver.mappers;

import org.example.fitpinserver.DAL.entities.BrandEntity;
import org.example.fitpinserver.DAL.entities.ProductEntity;
import org.example.fitpinserver.DAL.mappers.ProductPersistenceMapper;
import org.example.fitpinserver.domain.models.Product;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ProductPersistenceMapperTest {

    private final ProductPersistenceMapper mapper = new ProductPersistenceMapper();

    @Test
    void toDomain_Should_Map_All_Fields() {
        BrandEntity brandEntity = new BrandEntity("Nike");
        ReflectionTestUtils.setField(brandEntity, "id", 1L);

        ProductEntity entity = new ProductEntity("Air Force 1", brandEntity);
        ReflectionTestUtils.setField(entity, "id", 2L);
        entity.setImageUrl("https://example.com/product.jpg");

        Product result = mapper.toDomain(entity);

        assertEquals(2L, result.getId());
        assertEquals("Air Force 1", result.getName());
        assertEquals("https://example.com/product.jpg", result.getImageUrl());
        assertEquals(1L, result.getBrand().getId());
        assertEquals("Nike", result.getBrand().getName());
    }

    @Test
    void toDomain_Should_Map_Product_Without_ImageUrl() {
        BrandEntity brandEntity = new BrandEntity("Adidas");
        ReflectionTestUtils.setField(brandEntity, "id", 2L);

        ProductEntity entity = new ProductEntity("Stan Smith", brandEntity);
        ReflectionTestUtils.setField(entity, "id", 3L);

        Product result = mapper.toDomain(entity);

        assertEquals(3L, result.getId());
        assertEquals("Stan Smith", result.getName());
        assertNull(result.getImageUrl());
        assertEquals(2L, result.getBrand().getId());
        assertEquals("Adidas", result.getBrand().getName());
    }
}
