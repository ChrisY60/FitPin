package org.example.fitpinserver.DAL.repositories.elasticsearch;

import org.example.fitpinserver.DAL.elasticsearch.BrandDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface BrandSearchRepository extends ElasticsearchRepository<BrandDocument, Long> {

    @Query("""
        { "match": { "name": { "query": "?0", "fuzziness": "AUTO" } } }
        """)
    List<BrandDocument> searchByName(String query);
}
