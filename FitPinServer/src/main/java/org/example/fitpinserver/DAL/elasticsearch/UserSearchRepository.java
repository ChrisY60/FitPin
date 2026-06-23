package org.example.fitpinserver.DAL.elasticsearch;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface UserSearchRepository extends ElasticsearchRepository<UserDocument, Long> {

    @Query("""
        { "match": { "username": { "query": "?0", "fuzziness": "AUTO" } } }
        """)
    List<UserDocument> searchByUsername(String query);
}
