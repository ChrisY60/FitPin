package org.example.fitpinserver.services;

import org.example.fitpinserver.business.repositories.ProductRepository;
import org.example.fitpinserver.business.serviceImplementations.ProductServiceImpl;
import org.example.fitpinserver.domain.models.Brand;
import org.example.fitpinserver.domain.models.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void getAllProducts_Should_Return_All_Products() {
        Brand brand = new Brand(1L, "Nike");
        List<Product> expected = List.of(
                new Product(1L, "Air Force 1", brand),
                new Product(2L, "Jordan 1", brand)
        );
        when(productRepository.findAll()).thenReturn(expected);

        List<Product> result = productService.getAllProducts();

        assertEquals(2, result.size());
        assertEquals(expected, result);
        verify(productRepository).findAll();
    }

    @Test
    void getAllProducts_Should_Return_Empty_List_When_No_Products_Exist() {
        when(productRepository.findAll()).thenReturn(List.of());

        List<Product> result = productService.getAllProducts();

        assertTrue(result.isEmpty());
        verify(productRepository).findAll();
    }
}
