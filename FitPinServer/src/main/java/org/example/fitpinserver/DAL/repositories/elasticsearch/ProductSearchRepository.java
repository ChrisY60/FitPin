package org.example.fitpinserver.DAL.repositories.elasticsearch;

import org.example.fitpinserver.DAL.elasticsearch.ProductDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ProductSearchRepository extends ElasticsearchRepository<ProductDocument, Long> {

    @Query("""
        { "multi_match": { "query": "?0", "fields": ["name^2", "brandName"], "fuzziness": "AUTO" } }
        """)
    List<ProductDocument> searchByName(String query);
}
