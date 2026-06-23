package org.example.fitpinserver.DAL.repositories.elasticsearch;

import org.example.fitpinserver.DAL.elasticsearch.TagDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface TagSearchRepository extends ElasticsearchRepository<TagDocument, Long> {

    @Query("""
        { "match": { "name": { "query": "?0", "fuzziness": "AUTO" } } }
        """)
    List<TagDocument> searchByName(String query);
}
