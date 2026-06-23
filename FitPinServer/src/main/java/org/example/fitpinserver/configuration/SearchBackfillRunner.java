package org.example.fitpinserver.configuration;

import org.example.fitpinserver.DAL.elasticsearch.BrandDocument;
import org.example.fitpinserver.DAL.elasticsearch.ProductDocument;
import org.example.fitpinserver.DAL.elasticsearch.TagDocument;
import org.example.fitpinserver.DAL.elasticsearch.UserDocument;
import org.example.fitpinserver.business.repositories.BrandRepository;
import org.example.fitpinserver.business.repositories.ProductRepository;
import org.example.fitpinserver.business.repositories.TagRepository;
import org.example.fitpinserver.business.repositories.UserRepository;
import org.example.fitpinserver.business.services.SearchIndexService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * Products and brands have no write path through the application (they're seeded
 * directly into MySQL), so this is the only way to (re)populate their Elasticsearch
 * indices. Set APP_SEARCH_REINDEX_ON_STARTUP=true for one run, then unset it.
 * Index creation is deliberately not eager (see createIndex=false on the @Document
 * classes) so the app can start without a reachable Elasticsearch cluster; this runner
 * creates each index with its mapping on demand instead.
 */
@Component
public class SearchBackfillRunner implements ApplicationRunner {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final SearchIndexService searchIndexService;
    private final ElasticsearchOperations elasticsearchOperations;
    private final boolean reindexOnStartup;

    public SearchBackfillRunner(ProductRepository productRepository, BrandRepository brandRepository,
                                 UserRepository userRepository, TagRepository tagRepository,
                                 SearchIndexService searchIndexService,
                                 ElasticsearchOperations elasticsearchOperations,
                                 @Value("${app.search.reindex-on-startup:false}") boolean reindexOnStartup) {
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
        this.searchIndexService = searchIndexService;
        this.elasticsearchOperations = elasticsearchOperations;
        this.reindexOnStartup = reindexOnStartup;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!reindexOnStartup) {
            return;
        }

        ensureIndexExists(ProductDocument.class);
        ensureIndexExists(BrandDocument.class);
        ensureIndexExists(UserDocument.class);
        ensureIndexExists(TagDocument.class);

        productRepository.findAll().forEach(searchIndexService::indexProduct);
        brandRepository.findAll().forEach(searchIndexService::indexBrand);
        userRepository.findAll().forEach(searchIndexService::indexUser);
        tagRepository.findAll().forEach(searchIndexService::indexTag);
    }

    private void ensureIndexExists(Class<?> documentClass) {
        IndexOperations indexOps = elasticsearchOperations.indexOps(documentClass);
        if (!indexOps.exists()) {
            // Elastic serverless rejects index.number_of_shards/number_of_replicas, which
            // createWithMapping() sends by default, so create with no settings and add the
            // mapping separately instead.
            indexOps.create(Collections.emptyMap());
            indexOps.putMapping();
        }
    }
}
