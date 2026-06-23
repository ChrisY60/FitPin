package org.example.fitpinserver.DAL.repositories;

import org.example.fitpinserver.DAL.elasticsearch.TagSearchRepository;
import org.example.fitpinserver.DAL.mappers.TagPersistenceMapper;
import org.example.fitpinserver.business.repositories.TagRepository;
import org.example.fitpinserver.domain.models.Tag;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TagRepositoryImpl implements TagRepository {

    private final TagJPARepository tagJPARepository;
    private final TagPersistenceMapper tagPersistenceMapper;
    private final TagSearchRepository tagSearchRepository;

    public TagRepositoryImpl(TagJPARepository tagJPARepository, TagPersistenceMapper tagPersistenceMapper,
                              TagSearchRepository tagSearchRepository) {
        this.tagJPARepository = tagJPARepository;
        this.tagPersistenceMapper = tagPersistenceMapper;
        this.tagSearchRepository = tagSearchRepository;
    }

    @Override
    public List<Tag> findAll() {
        return tagJPARepository.findAll().stream()
                .map(tagPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<Tag> searchByName(String query) {
        return tagSearchRepository.searchByName(query).stream()
                .map(document -> new Tag(document.getId(), document.getName()))
                .toList();
    }
}
