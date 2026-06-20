package org.example.fitpinserver.services;

import org.example.fitpinserver.business.repositories.BrandRepository;
import org.example.fitpinserver.business.repositories.ProductRepository;
import org.example.fitpinserver.business.repositories.TagRepository;
import org.example.fitpinserver.business.repositories.UserRepository;
import org.example.fitpinserver.business.serviceImplementations.SearchServiceImpl;
import org.example.fitpinserver.domain.models.Brand;
import org.example.fitpinserver.domain.models.Product;
import org.example.fitpinserver.domain.models.Tag;
import org.example.fitpinserver.domain.models.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private SearchServiceImpl searchService;

    @Test
    void searchProducts_Should_Delegate_To_Repository_With_Trimmed_Query() {
        Brand brand = new Brand(1L, "Nike");
        List<Product> expected = List.of(new Product(1L, "Air Force 1", brand));
        when(productRepository.searchByName("air")).thenReturn(expected);

        List<Product> result = searchService.searchProducts("  air  ");

        assertEquals(expected, result);
        verify(productRepository).searchByName("air");
    }

    @Test
    void searchBrands_Should_Delegate_To_Repository() {
        List<Brand> expected = List.of(new Brand(1L, "Nike"));
        when(brandRepository.searchByName("nik")).thenReturn(expected);

        List<Brand> result = searchService.searchBrands("nik");

        assertEquals(expected, result);
        verify(brandRepository).searchByName("nik");
    }

    @Test
    void searchUsers_Should_Delegate_To_Repository() {
        List<User> expected = List.of(new User(1L, "chris", "chris@example.com", null, "hash"));
        when(userRepository.searchByUsername("chr")).thenReturn(expected);

        List<User> result = searchService.searchUsers("chr");

        assertEquals(expected, result);
        verify(userRepository).searchByUsername("chr");
    }

    @Test
    void searchTags_Should_Delegate_To_Repository() {
        List<Tag> expected = List.of(new Tag(1L, "summer"));
        when(tagRepository.searchByName("sum")).thenReturn(expected);

        List<Tag> result = searchService.searchTags("sum");

        assertEquals(expected, result);
        verify(tagRepository).searchByName("sum");
    }

    @Test
    void searchProducts_Should_Treat_Null_Query_As_Empty() {
        when(productRepository.searchByName("")).thenReturn(List.of());

        searchService.searchProducts(null);

        verify(productRepository).searchByName("");
    }
}
