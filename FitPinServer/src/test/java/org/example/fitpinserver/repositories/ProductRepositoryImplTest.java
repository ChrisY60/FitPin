package org.example.fitpinserver.repositories;

import org.example.fitpinserver.DAL.entities.BrandEntity;
import org.example.fitpinserver.DAL.entities.ProductEntity;
import org.example.fitpinserver.DAL.mappers.ProductPersistenceMapper;
import org.example.fitpinserver.DAL.repositories.ProductJPARepository;
import org.example.fitpinserver.DAL.repositories.ProductRepositoryImpl;
import org.example.fitpinserver.domain.models.Brand;
import org.example.fitpinserver.domain.models.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryImplTest {

    @Mock
    private ProductJPARepository productJPARepository;

    @Mock
    private ProductPersistenceMapper productPersistenceMapper;

    @InjectMocks
    private ProductRepositoryImpl productRepository;

    private ProductEntity makeEntity(Long id, String name) {
        BrandEntity brand = new BrandEntity("Nike");
        ReflectionTestUtils.setField(brand, "id", 1L);
        ProductEntity entity = new ProductEntity(name, brand);
        ReflectionTestUtils.setField(entity, "id", id);
        return entity;
    }

    private Product makeDomain(Long id, String name) {
        return new Product(id, name, new Brand(1L, "Nike"));
    }

    @Test
    void findAll_Should_Return_All_Mapped_Products() {
        ProductEntity e1 = makeEntity(1L, "Air Force 1");
        ProductEntity e2 = makeEntity(2L, "Jordan 1");
        Product p1 = makeDomain(1L, "Air Force 1");
        Product p2 = makeDomain(2L, "Jordan 1");

        when(productJPARepository.findAll()).thenReturn(List.of(e1, e2));
        when(productPersistenceMapper.toDomain(e1)).thenReturn(p1);
        when(productPersistenceMapper.toDomain(e2)).thenReturn(p2);

        List<Product> result = productRepository.findAll();

        assertEquals(2, result.size());
        assertEquals(p1, result.get(0));
        assertEquals(p2, result.get(1));
    }

    @Test
    void findAll_Should_Return_Empty_List_When_No_Products_Exist() {
        when(productJPARepository.findAll()).thenReturn(List.of());

        List<Product> result = productRepository.findAll();

        assertTrue(result.isEmpty());
    }

    @Test
    void findAllByIds_Should_Return_Products_For_Given_Ids() {
        List<Long> ids = List.of(1L, 2L);
        ProductEntity e1 = makeEntity(1L, "Shirt");
        Product p1 = makeDomain(1L, "Shirt");

        when(productJPARepository.findAllById(ids)).thenReturn(List.of(e1));
        when(productPersistenceMapper.toDomain(e1)).thenReturn(p1);

        List<Product> result = productRepository.findAllByIds(ids);

        assertEquals(1, result.size());
        assertEquals(p1, result.get(0));
    }

    @Test
    void findAllByIds_Should_Return_Empty_List_When_Ids_Are_Empty() {
        List<Product> result = productRepository.findAllByIds(List.of());

        assertTrue(result.isEmpty());
        verify(productJPARepository, never()).findAllById(any());
    }

    @Test
    void findAllByIds_Should_Return_Empty_List_When_Ids_Are_Null() {
        List<Product> result = productRepository.findAllByIds(null);

        assertTrue(result.isEmpty());
        verify(productJPARepository, never()).findAllById(any());
    }
}
