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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;

@Service
public class SearchServiceImpl implements SearchService {

    private static final Logger log = LoggerFactory.getLogger(SearchServiceImpl.class);

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
        return searchBestEffort(() -> productRepository.searchByName(normalize(query)));
    }

    @Override
    public List<Brand> searchBrands(String query) {
        return searchBestEffort(() -> brandRepository.searchByName(normalize(query)));
    }

    @Override
    public List<User> searchUsers(String query) {
        return searchBestEffort(() -> userRepository.searchByUsername(normalize(query)));
    }

    @Override
    public List<Tag> searchTags(String query) {
        return searchBestEffort(() -> tagRepository.searchByName(normalize(query)));
    }

    private String normalize(String query) {
        return query == null ? "" : query.trim();
    }

    /**
     * Elasticsearch being slow or unreachable should degrade search to empty
     * results rather than fail the whole request.
     */
    private <T> List<T> searchBestEffort(Supplier<List<T>> search) {
        try {
            return search.get();
        } catch (Exception e) {
            log.warn("Elasticsearch search failed, returning empty results: {}", e.getMessage());
            return List.of();
        }
    }
}
