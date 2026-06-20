package org.example.fitpinserver.business.serviceImplementations;

import org.example.fitpinserver.business.repositories.BrandRepository;
import org.example.fitpinserver.business.repositories.ProductRepository;
import org.example.fitpinserver.business.repositories.TagRepository;
import org.example.fitpinserver.business.repositories.UserRepository;
import org.example.fitpinserver.business.services.SearchService;
import org.example.fitpinserver.domain.models.Brand;
import org.example.fitpinserver.domain.models.Product;
import org.example.fitpinserver.domain.models.Tag;
import org.example.fitpinserver.domain.models.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    public SearchServiceImpl(ProductRepository productRepository, BrandRepository brandRepository,
                             UserRepository userRepository, TagRepository tagRepository) {
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public List<Product> searchProducts(String query) {
        return productRepository.searchByName(normalize(query));
    }

    @Override
    public List<Brand> searchBrands(String query) {
        return brandRepository.searchByName(normalize(query));
    }

    @Override
    public List<User> searchUsers(String query) {
        return userRepository.searchByUsername(normalize(query));
    }

    @Override
    public List<Tag> searchTags(String query) {
        return tagRepository.searchByName(normalize(query));
    }

    private String normalize(String query) {
        return query == null ? "" : query.trim();
    }
}
