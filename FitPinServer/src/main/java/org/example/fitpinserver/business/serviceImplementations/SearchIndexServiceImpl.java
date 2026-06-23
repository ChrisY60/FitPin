package org.example.fitpinserver.business.serviceImplementations;

import org.example.fitpinserver.DAL.elasticsearch.BrandDocument;
import org.example.fitpinserver.DAL.elasticsearch.BrandSearchRepository;
import org.example.fitpinserver.DAL.elasticsearch.ProductDocument;
import org.example.fitpinserver.DAL.elasticsearch.ProductSearchRepository;
import org.example.fitpinserver.DAL.elasticsearch.TagDocument;
import org.example.fitpinserver.DAL.elasticsearch.TagSearchRepository;
import org.example.fitpinserver.DAL.elasticsearch.UserDocument;
import org.example.fitpinserver.DAL.elasticsearch.UserSearchRepository;
import org.example.fitpinserver.business.services.SearchIndexService;
import org.example.fitpinserver.domain.models.Brand;
import org.example.fitpinserver.domain.models.Product;
import org.example.fitpinserver.domain.models.Tag;
import org.example.fitpinserver.domain.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Indexing is best-effort: a slow or unreachable Elasticsearch cluster must never
 * fail the primary MySQL write it's piggybacking on (signup, profile update, post
 * creation), so every call swallows and logs instead of propagating.
 */
@Service
public class SearchIndexServiceImpl implements SearchIndexService {

    private static final Logger log = LoggerFactory.getLogger(SearchIndexServiceImpl.class);

    private final ProductSearchRepository productSearchRepository;
    private final BrandSearchRepository brandSearchRepository;
    private final UserSearchRepository userSearchRepository;
    private final TagSearchRepository tagSearchRepository;

    public SearchIndexServiceImpl(ProductSearchRepository productSearchRepository,
                                   BrandSearchRepository brandSearchRepository,
                                   UserSearchRepository userSearchRepository,
                                   TagSearchRepository tagSearchRepository) {
        this.productSearchRepository = productSearchRepository;
        this.brandSearchRepository = brandSearchRepository;
        this.userSearchRepository = userSearchRepository;
        this.tagSearchRepository = tagSearchRepository;
    }

    @Override
    public void indexUser(User user) {
        runBestEffort(() -> userSearchRepository.save(
                new UserDocument(user.getId(), user.getUsername(), user.getProfilePictureUrl())));
    }

    @Override
    public void deleteUser(Long userId) {
        runBestEffort(() -> userSearchRepository.deleteById(userId));
    }

    @Override
    public void indexTag(Tag tag) {
        runBestEffort(() -> tagSearchRepository.save(new TagDocument(tag.getId(), tag.getName())));
    }

    @Override
    public void indexProduct(Product product) {
        runBestEffort(() -> {
            Brand brand = product.getBrand();
            productSearchRepository.save(new ProductDocument(
                    product.getId(),
                    product.getName(),
                    brand == null ? null : brand.getId(),
                    brand == null ? null : brand.getName(),
                    product.getImageUrl()
            ));
        });
    }

    @Override
    public void indexBrand(Brand brand) {
        runBestEffort(() -> brandSearchRepository.save(new BrandDocument(brand.getId(), brand.getName())));
    }

    private void runBestEffort(Runnable indexingOperation) {
        try {
            indexingOperation.run();
        } catch (Exception e) {
            log.warn("Elasticsearch indexing failed, continuing without it: {}", e.getMessage());
        }
    }
}
